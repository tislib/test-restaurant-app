package net.tislib.restaurantapp.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.config.JwtConfig;
import net.tislib.restaurantapp.controller.AuthenticationController;
import net.tislib.restaurantapp.controller.UserController;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenPair.TokenDetails;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import net.tislib.restaurantapp.data.authentication.UserAuthority;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.data.mapper.UserMapper;
import net.tislib.restaurantapp.exception.UserAlreadyExistsException;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String TOKEN_DETAILS = "tokenDetails";
    public static final String INVALID_AUTHENTICATION_MESSAGE = "request is not properly authenticated";
    public static final String BAD_CREDENTIALS_MESSAGE = "username or password is incorrect";

    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ROLE = "userRole";
    private static final String INVALID_TOKEN_TYPE_MESSAGE = "invalid token type is accepted: {}";

    private JwtParser tokenParser;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtConfig jwtConfig;

    @PostConstruct
    public void init() {
        log.info("initialize token parser");

        // initialize tokenParser ( tokenParser is used for both access tokens and refresh tokens )
        tokenParser = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    /*
      This method is responsible for authentication by email and password
      returning result contains
     */
    public TokenPair token(TokenCreateRequest tokenCreateRequest) {
        log.trace("token request / request user: {}", tokenCreateRequest.getEmail());

        // authenticate user with credentials
        User user = authenticateUser(tokenCreateRequest);

        log.debug("token request / user authenticated: {} {}", user.getEmail(), user.getId());

        // tokenPair is a container for access and refresh tokens
        TokenPair tokenPair = new TokenPair();

        tokenPair.setAccessToken(prepareAccessToken(user));
        tokenPair.setRefreshToken(prepareRefreshToken(user));

        // prepare hateoas link token details endpoint
        tokenPair.add(linkTo(methodOn(AuthenticationController.class).getToken())
                .withRel(TOKEN_DETAILS));

        log.info("token generated for user: {}", tokenCreateRequest.getEmail());

        return tokenPair;
    }

    private User authenticateUser(TokenCreateRequest tokenCreateRequest) {
        log.trace("user authentication / email: {}", tokenCreateRequest.getEmail());

        User user = userRepository.findByEmail(tokenCreateRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

        log.debug("user found: {} {}", user.getEmail(), user.getId());

        if (!passwordEncoder.matches(tokenCreateRequest.getPassword(), user.getPassword())) {
            log.info("password is wrong for user: {} {}", user.getEmail(), user.getId());
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }

        log.info("user authenticated: {}", user);

        return user;
    }

    private TokenDetails prepareRefreshToken(User user) {
        log.debug("preparing refresh token for user: {}", user.getEmail());
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtConfig.getRefreshTokenDurationSeconds()));

        // build refresh token
        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER_ID, user.getEmail())
                        .claim(TOKEN_TYPE, TOKEN_TYPE_REFRESH) // token type is used to differentiate access token and refresh token
                        .signWith(Keys.hmacShaKeyFor(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8)))
                        .compact())
                .build();
    }

    private TokenDetails prepareAccessToken(User user) {
        log.debug("preparing access token for user: {}", user.getEmail());

        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtConfig.getAccessTokenDurationSeconds()));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER_ROLE, user.getRole())
                        .claim(USER_EMAIL, user.getEmail())
                        .claim(USER_ID, user.getId())
                        .claim(TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                        .signWith(Keys.hmacShaKeyFor(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8)))
                        .compact())
                .build();
    }

    @Override
    public UserResource register(UserRegistrationRequest request) {
        log.trace("user registration request: email: {}; fullName: {}", request.getEmail(), request.getFullName());

        /*
            validate user request data, most of validations is done by annotation based validation rules
            validation method is used for handling complex validation which
         */
        validateRegistrationRequest(request);

        // prepare user data
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        // encode user password with bcrypt encoding
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        UserResource resource = userMapper.to(user);

        log.info("user registered with details: {}", user);

        // prepare resource links
        return resource.add(linkTo(methodOn(UserController.class)
                .get(user.getId())).withSelfRel());
    }

    private void validateRegistrationRequest(UserRegistrationRequest request) {
        // check for email uniqueness
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("user is already exists");
        }
    }

    @Override
    public TokenDetails refresh(String refreshToken) {
        log.trace("refresh token request");

        Jwt<?, Claims> jwtData = tokenParser.parseClaimsJws(refreshToken);
        log.debug("refresh token is parsed");
        Claims body = jwtData.getBody();

        String tokenType = body.get(TOKEN_TYPE, String.class);

        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
            log.warn(INVALID_TOKEN_TYPE_MESSAGE, tokenType);
            throw new InsufficientAuthenticationException("different token is supplied");
        }

        long userId = body.get(USER_ID, Number.class).longValue();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found with id: " + userId));

        log.debug("token refreshed for user: {}", user);

        return prepareAccessToken(user);
    }

    // preparing authentication object for spring security
    @Override
    public Optional<TokenAuthentication> getTokenAuthentication(String token) {
        try {
            Jwt<?, Claims> jwtData = tokenParser.parseClaimsJws(token);
            Claims body = jwtData.getBody();

            String tokenType = body.get(TOKEN_TYPE, String.class);

            /* only accept access tokens, refresh token should be ignored
               we have same signing key for access and refresh tokens, so an attacker may use refresh token to get some data
               by checking token type we prevent token misuse
             */

            if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
                log.warn(INVALID_TOKEN_TYPE_MESSAGE, tokenType);
                return Optional.empty();
            }

            /*
             we build Authentication object (TokenAuthentication) from jwt details, no database touch needed
             user role is used as token authority which will be used by checking user privileges by spring security
             */
            return Optional.of(TokenAuthentication.builder()
                    .name(body.get(USER_EMAIL, String.class))
                    .authorities(Collections.singleton(UserAuthority.builder()
                            .authority(body.get(USER_ROLE, String.class))
                            .build()))
                    .principal(jwtData)
                    .details(body.get(USER_EMAIL))
                    .userId(body.get(USER_ID, Number.class).longValue())
                    .build());
        } catch (JwtException e) { // if something is wrong with jwt parsing and authenticating
            // we will return null which means that authentication is finished with no success
            log.warn(e.getMessage(), e);
        }

        // just return empty to claim that authentication is finished
        return Optional.empty();
    }

    @Override
    public TokenUserDetails getTokenInfo() {
        log.trace("request for token ifo");

        TokenAuthentication tokenAuthentication = getCurrentAuthentication()
                .orElseThrow(() -> new InsufficientAuthenticationException(INVALID_AUTHENTICATION_MESSAGE));

        log.debug("authentication claimed: {}", tokenAuthentication.getName());

        Jwt<?, ?> jwtData = (Jwt<?, ?>) tokenAuthentication.getPrincipal();
        Claims claims = (Claims) jwtData.getBody();

        User user = userRepository.findById(claims.get(USER_ID, Number.class).longValue()).orElseThrow();

        log.debug("user found with authentication data: " + user);

        return TokenUserDetails.builder()
                .user(userMapper.to(user)
                        .add(linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel()))
                .creationTime(claims.getIssuedAt().toInstant())
                .expirationTime(claims.getExpiration().toInstant())
                .build()
                .add(linkTo(methodOn(AuthenticationController.class).getToken()).withSelfRel());
    }

    // this method is used by services to get current authenticated user by authentication
    @Override
    public User getCurrentUser() {
        TokenAuthentication tokenAuthentication = getCurrentAuthentication()
                .orElseThrow(() -> new InsufficientAuthenticationException(INVALID_AUTHENTICATION_MESSAGE));

        Jwt<?, ?> jwtData = (Jwt<?, ?>) tokenAuthentication.getPrincipal();
        Claims claims = (Claims) jwtData.getBody();

        // return user from userId which is stored in jwt token
        return userRepository.findById(claims.get(USER_ID, Number.class).longValue()).orElseThrow();
    }

    public Optional<TokenAuthentication> getCurrentAuthentication() {
        // read current authentication from spring security context
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(item -> item instanceof TokenAuthentication)
                .map(item -> (TokenAuthentication) item);
    }
}

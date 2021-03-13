package net.tislib.restaurantapp.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
import org.springframework.beans.factory.annotation.Value;
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
    private static final String EMAIL = "email";
    private static final String USER = "user";
    private static final String USER_ROLE = "userRole";
    private static final String INVALID_TOKEN_TYPE_MESSAGE = "invalid token type is accepted: {}";

    private JwtParser tokenParser;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtConfig jwtConfig;

    @PostConstruct
    public void init() {
        log.debug("initialize token parser");
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
        User user = userRepository.findByEmail(tokenCreateRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

        log.debug("user found: {} {}", user.getEmail(), user.getId());

        if (!passwordEncoder.matches(tokenCreateRequest.getPassword(), user.getPassword())) {
            log.info("password is wrong for user: {} {}", user.getEmail(), user.getId());
            throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
        }

        return user;
    }

    private TokenDetails prepareRefreshToken(User user) {
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtConfig.getRefreshTokenDurationSeconds()));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER, user)
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, TOKEN_TYPE_REFRESH)
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
                        .claim(USER, user)
                        .claim(USER_ROLE, user.getRole())
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                        .signWith(Keys.hmacShaKeyFor(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8)))
                        .compact())
                .build();
    }

    @Override
    public UserResource register(UserRegistrationRequest request) {
        validateRegistrationRequest(request);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        UserResource resource = userMapper.to(user);

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
        Jwt<?, Claims> jwtData = tokenParser.parseClaimsJws(refreshToken);
        Claims body = jwtData.getBody();

        String tokenType = body.get(TOKEN_TYPE, String.class);

        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
            log.warn(INVALID_TOKEN_TYPE_MESSAGE, tokenType);
            throw new InsufficientAuthenticationException("different token is supplied");
        }

        String email = body.get(EMAIL, String.class);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return prepareAccessToken(user);
    }

    @Override
    public TokenAuthentication getTokenAuthentication(String token) {
        try {
            Jwt<?, Claims> jwtData = tokenParser.parseClaimsJws(token);
            Claims body = jwtData.getBody();

            String tokenType = body.get(TOKEN_TYPE, String.class);

            if (!TOKEN_TYPE_ACCESS.equals(tokenType)) {
                log.warn(INVALID_TOKEN_TYPE_MESSAGE, tokenType);
                return null;
            }

            return TokenAuthentication.builder()
                    .name(body.get(EMAIL, String.class))
                    .authorities(Collections.singleton(UserAuthority.builder()
                            .authority(body.get(USER_ROLE, String.class))
                            .build()))
                    .principal(jwtData)
                    .details(body.get(USER))
                    .build();
        } catch (SignatureException e) {
            log.warn(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public TokenUserDetails getTokenInfo() {
        TokenAuthentication tokenAuthentication = getCurrentAuthentication()
                .orElseThrow(() -> new InsufficientAuthenticationException(INVALID_AUTHENTICATION_MESSAGE));

        Jwt<?, ?> jwtData = (Jwt<?, ?>) tokenAuthentication.getPrincipal();
        Claims claims = (Claims) jwtData.getBody();

        User user = userRepository.findByEmail(claims.get(EMAIL, String.class)).orElseThrow();

        return TokenUserDetails.builder()
                .user(userMapper.to(user)
                        .add(linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel()))
                .creationTime(claims.getIssuedAt().toInstant())
                .expirationTime(claims.getExpiration().toInstant())
                .build()
                .add(linkTo(methodOn(AuthenticationController.class).getToken()).withSelfRel());
    }

    @Override
    public User getCurrentUser() {
        TokenAuthentication tokenAuthentication = getCurrentAuthentication()
                .orElseThrow(() -> new InsufficientAuthenticationException(INVALID_AUTHENTICATION_MESSAGE));

        Jwt<?, ?> jwtData = (Jwt<?, ?>) tokenAuthentication.getPrincipal();
        Claims claims = (Claims) jwtData.getBody();

        return userRepository.findByEmail(claims.get(EMAIL, String.class)).orElseThrow();
    }

    public Optional<TokenAuthentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(item -> item instanceof TokenAuthentication)
                .map(item -> (TokenAuthentication) item);
    }
}

package net.tislib.restaurantapp.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.controller.UserController;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenPair.TokenDetails;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.data.mapper.UserMapper;
import net.tislib.restaurantapp.exception.UserAlreadyExistsException;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String EMAIL = "email";
    private static final String USER = "user";

    @Value("${jwt.signKey}")
    private String jwtTokenSignKey;

    @Value("${jwt.accessTokenDurationSeconds}")
    private int jwtAccessTokenDurationSeconds;

    @Value("${jwt.refreshTokenDurationSeconds}")
    private int jwtRefreshTokenDurationSeconds;

    private JwtParser tokenParser;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        tokenParser = Jwts.parserBuilder()
                .setSigningKey(jwtTokenSignKey.getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    public TokenPair token(TokenCreateRequest tokenCreateRequest) {
        User user = authenticateUser(tokenCreateRequest);

        TokenPair tokenPair = new TokenPair();

        tokenPair.setAccessToken(prepareAccessToken(user));
        tokenPair.setRefreshToken(prepareRefreshToken(user));

        return tokenPair;
    }

    private User authenticateUser(TokenCreateRequest tokenCreateRequest) {
        User user = userRepository.findByEmail(tokenCreateRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(tokenCreateRequest.getEmail()));

        if (!passwordEncoder.matches(tokenCreateRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("username or password is incorrect");
        }

        return user;
    }

    private TokenDetails prepareRefreshToken(User user) {
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtRefreshTokenDurationSeconds));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER, user)
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, REFRESH)
                        .signWith(Keys.hmacShaKeyFor(jwtTokenSignKey.getBytes(StandardCharsets.UTF_8)))
                        .compact())
                .build();
    }

    private TokenDetails prepareAccessToken(User user) {
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtAccessTokenDurationSeconds));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER, user)
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, ACCESS)
                        .signWith(Keys.hmacShaKeyFor(jwtTokenSignKey.getBytes(StandardCharsets.UTF_8)))
                        .compact())
                .build();
    }

    @Override
    public UserResource register(UserRegistrationRequest request) {
        validateRegistrationRequest(request);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        return userMapper.to(user)
                .add(linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel());
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

        if (!REFRESH.equals(tokenType)) {
            log.warn("invalid token type is accepted: {}", tokenType);
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

            if (!ACCESS.equals(tokenType)) {
                log.warn("invalid token type is accepted: {}", tokenType);
                return null;
            }

            return TokenAuthentication.builder()
                    .name(body.get(EMAIL, String.class))
                    .authorities(new HashSet<>())
                    .principal(jwtData)
                    .details(body.get(USER))
                    .build();
        } catch (SignatureException e) {
            log.warn(e.getMessage(), e);
        } catch (Throwable e) {
            log.debug(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public TokenUserDetails getTokenInfo() {
        TokenAuthentication tokenAuthentication = getCurrentAuthentication()
                .orElseThrow(() -> new InsufficientAuthenticationException("request is not properly authenticated"));

        Jwt<?, ?> jwtData = (Jwt<?, ?>) tokenAuthentication.getPrincipal();
        Claims claims = (Claims) jwtData.getBody();

        return TokenUserDetails.builder()
                .email(claims.get(EMAIL, String.class))
                .creationTime(claims.getIssuedAt().toInstant())
                .expirationTime(claims.getExpiration().toInstant())
                .build();
    }

    public Optional<TokenAuthentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(item -> item instanceof TokenAuthentication)
                .map(item -> (TokenAuthentication) item);
    }
}

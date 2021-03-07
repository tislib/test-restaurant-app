package net.tislib.restaurantapp.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenPair.TokenDetails;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

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

    @PostConstruct
    public void init() {
        tokenParser = Jwts.parserBuilder()
                .setSigningKey(jwtTokenSignKey.getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    public TokenPair token(TokenCreateRequest tokenCreateRequest) {
        User user = authenticateUser(tokenCreateRequest);

        Key key = Keys.hmacShaKeyFor(jwtTokenSignKey.getBytes(StandardCharsets.UTF_8));


        TokenPair tokenPair = new TokenPair();

        tokenPair.setAccessToken(prepareAccessToken(user, key));
        tokenPair.setRefreshToken(prepareRefreshToken(user, key));

        return tokenPair;
    }

    private User authenticateUser(TokenCreateRequest tokenCreateRequest) {
        return null;
    }

    private TokenDetails prepareRefreshToken(User user, Key key) {
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtRefreshTokenDurationSeconds));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER, user)
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, REFRESH)
                        .signWith(key)
                        .compact())
                .build();
    }

    private TokenDetails prepareAccessToken(User user, Key key) {
        Instant expiry = Instant.now().plus(Duration.ofSeconds(jwtAccessTokenDurationSeconds));

        return TokenDetails.builder()
                .expiry(expiry)
                .content(Jwts.builder()
                        .setIssuedAt(new Date())
                        .setExpiration(Date.from(expiry))
                        .claim(USER, user)
                        .claim(EMAIL, user.getEmail())
                        .claim(TOKEN_TYPE, ACCESS)
                        .signWith(key)
                        .compact())
                .build();
    }

    @Override
    public UserResource register(UserRegistrationRequest request) {
        return null;
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
                    .principal(body.get(USER, User.class))
                    .build();
        } catch (SignatureException e) {
            log.warn(e.getMessage(), e);
        } catch (Throwable e) {
            log.debug(e.getMessage(), e);
        }

        return null;
    }

    public Optional<TokenAuthentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(item -> item instanceof TokenAuthentication)
                .map(item -> (TokenAuthentication) item);
    }
}

package net.tislib.restaurantapp.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
public class JwtConfig {

    private String tokenSignKey;

    private int accessTokenDurationSeconds;

    private int refreshTokenDurationSeconds;

}

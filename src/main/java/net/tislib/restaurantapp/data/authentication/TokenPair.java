package net.tislib.restaurantapp.data.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class TokenPair extends RepresentationModel<TokenPair> {

    private TokenDetails accessToken;
    private TokenDetails refreshToken;

    @Data
    public static class TokenDetails {
        private String content;
        private Instant expiry;
    }

}

package net.tislib.restaurantapp.data.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair extends RepresentationModel<TokenPair> {

    private TokenDetails accessToken;
    private TokenDetails refreshToken;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenDetails {
        private String content;
        private Instant expiry;
    }

}

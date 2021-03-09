package net.tislib.restaurantapp.data.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenUserDetails {

    private Instant creationTime;

    private Instant expirationTime;

    private UserResource user;
}

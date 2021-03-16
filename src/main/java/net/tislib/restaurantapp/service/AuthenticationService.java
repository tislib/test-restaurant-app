package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.model.User;

import java.util.Optional;

public interface AuthenticationService {
    TokenPair token(TokenCreateRequest tokenCreateRequest);

    UserResource register(UserRegistrationRequest request);

    Optional<TokenAuthentication> getTokenAuthentication(String token);

    TokenUserDetails getTokenInfo();

    TokenPair.TokenDetails refresh(String refreshToken);

    User getCurrentUser();
}

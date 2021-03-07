package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;

public interface AuthenticationService {
    TokenPair token(TokenCreateRequest tokenCreateRequest);

    UserResource register(UserRegistrationRequest request);

    TokenAuthentication getTokenAuthentication(String token);
}

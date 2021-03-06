package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public TokenPair token(TokenCreateRequest tokenCreateRequest) {
        return null;
    }

    @Override
    public UserResource register(UserRegistrationRequest request) {
        return null;
    }
}

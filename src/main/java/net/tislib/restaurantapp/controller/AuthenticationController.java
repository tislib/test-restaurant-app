package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;

@RestController
@RequestMapping(API_AUTHENTICATION)
@RequiredArgsConstructor
@Tag(name = "authentication", description = "endpoints related to authentication and registration")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(PATH_TOKEN)
    public TokenPair token(@RequestBody @Validated TokenCreateRequest tokenCreateRequest) {
        return service.token(tokenCreateRequest);
    }

    @PostMapping
    public UserResource register(@RequestBody @Validated UserRegistrationRequest request) {
        return service.register(request);
    }

}

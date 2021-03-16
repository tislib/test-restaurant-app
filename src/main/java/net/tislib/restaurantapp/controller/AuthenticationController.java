package net.tislib.restaurantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenRefreshRequest;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_REGISTER;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;

@RestController
@RequestMapping(API_AUTHENTICATION)
@RequiredArgsConstructor
@Tag(name = "authentication", description = "endpoints related to authentication and registration")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(PATH_TOKEN)
    @Operation(summary = "login", description = "authenticate user with credentials and create user")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenPair createToken(@RequestBody @Validated TokenCreateRequest tokenCreateRequest) {
        return service.token(tokenCreateRequest);
    }

    @GetMapping(PATH_TOKEN)
    @Operation(summary = "current token info", description = "get current token info")
    public TokenUserDetails getToken() {
        return service.getTokenInfo();
    }

    @PatchMapping(PATH_TOKEN)
    @Operation(summary = "refresh access token", description = "create new access token via refresh token")
    public TokenPair.TokenDetails refreshToken(@RequestBody @Validated TokenRefreshRequest tokenRefreshRequest) {
        return service.refresh(tokenRefreshRequest.getRefreshToken());
    }

    @PostMapping(PATH_REGISTER)
    @Operation(summary = "register user", description = "user registration")
    public UserResource register(@RequestBody @Validated UserRegistrationRequest request) {
        return service.register(request);
    }

}

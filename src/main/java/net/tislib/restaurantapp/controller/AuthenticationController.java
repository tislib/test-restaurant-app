package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constants.ApiConstants.API_AUTHENTICATION;

@RestController
@RequestMapping(API_AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    @PostMapping
    public void authenticate() {

    }

}

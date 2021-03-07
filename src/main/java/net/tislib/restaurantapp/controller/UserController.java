package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;

@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/{id}")
    public UserResource get(@PathVariable Long id) {
        return null;
    }

}

package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.UserResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;

@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
public class UserController {

    @PostMapping
    public UserResource create(@RequestBody @Validated UserResource userResource) {
        return null;
    }

    @GetMapping
    public List<UserResource> list() {
        return null;
    }

    @GetMapping("/{id}")
    public UserResource get(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public UserResource update(@PathVariable Long id,
                               @RequestBody @Validated UserResource userResource) {
        return null;
    }

}

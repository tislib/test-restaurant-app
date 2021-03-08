package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.tislib.restaurantapp.constant.ApiConstants.API_RESTAURANTS;

@RestController
@RequestMapping(API_RESTAURANTS)
@RequiredArgsConstructor
public class RestaurantController {

    @PostMapping
    public RestaurantResource create(@RequestBody @Validated RestaurantResource userResource) {
        return null;
    }

    @GetMapping
    public PageContainer<RestaurantResource> list() {
        return null;
    }

    @GetMapping("/{id}")
    public RestaurantResource get(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public RestaurantResource update(@PathVariable Long id,
                               @RequestBody @Validated RestaurantResource userResource) {
        return null;
    }

}

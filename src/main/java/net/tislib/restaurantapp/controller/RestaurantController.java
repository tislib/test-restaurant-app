package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping
    public void create(final @RequestBody @Validated Restaurant restaurant) {
        service.create(restaurant);
    }

    @GetMapping
    public List<Restaurant> list() {
        return service.list();
    }

}

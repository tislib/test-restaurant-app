package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantResource create(final RestaurantResource restaurant) {
        restaurant.setId(null);
        repository.save(restaurant);
        return restaurant;
    }

    @Override
    public List<Restaurant> list(BigDecimal rating, Long ownerId) {
        return repository.findAll();
    }

}

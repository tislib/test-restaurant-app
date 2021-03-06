package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantRepository;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    public void create(final Restaurant restaurant) {
        repository.save(restaurant);
    }

    @Override
    public List<Restaurant> list() {
        return repository.findAll();
    }

}

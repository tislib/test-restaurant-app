package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    void create(Restaurant restaurant);

    List<Restaurant> list();
}

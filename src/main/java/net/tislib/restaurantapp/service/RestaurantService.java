package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantService {
    RestaurantResource create(RestaurantResource restaurant);

    PageContainer<RestaurantResource> list(BigDecimal rating, Long ownerId);

    RestaurantResource get(Long id);

    RestaurantResource update(Long id, RestaurantResource resource);

    RestaurantResource delete(Long id);
}

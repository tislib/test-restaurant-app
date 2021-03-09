package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface RestaurantService {
    RestaurantResource create(RestaurantResource restaurant);

    PageContainer<RestaurantResource> list(BigDecimal rating, Long ownerId, Pageable pageable);

    RestaurantResource get(Long id);

    RestaurantResource update(Long id, RestaurantResource resource);

    RestaurantResource delete(Long id);
}

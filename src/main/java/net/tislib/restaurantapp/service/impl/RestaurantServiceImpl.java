package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.data.mapper.RestaurantMapper;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.service.RestaurantService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public RestaurantResource create(final RestaurantResource restaurant) {
        restaurant.setId(null);
//        repository.save(restaurant);
        return restaurant;
    }

    @Override
    public PageContainer<RestaurantResource> list(BigDecimal rating, Long ownerId, Pageable pageable) {
        return mapper.mapPage(repository.findAll(pageable));
    }

    @Override
    public RestaurantResource get(Long id) {
        return null;
    }

    @Override
    public RestaurantResource update(Long id, RestaurantResource resource) {
        return null;
    }

    @Override
    public RestaurantResource delete(Long id) {
        return null;
    }

}

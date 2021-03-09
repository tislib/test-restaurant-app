package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface RestaurantMapper extends ResourceEntityMapper<RestaurantResource, Restaurant> {

    @Mapping(source = RestaurantReviewStats.Fields.ratingAverage, target = RestaurantResource.Fields.rating)
    void mapReviews(@MappingTarget RestaurantResource restaurant, RestaurantReviewStats reviewStats);

}

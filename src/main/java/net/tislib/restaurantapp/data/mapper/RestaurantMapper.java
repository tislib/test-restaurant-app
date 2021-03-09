package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper extends ResourceEntityMapper<RestaurantResource, Restaurant> {
    
}

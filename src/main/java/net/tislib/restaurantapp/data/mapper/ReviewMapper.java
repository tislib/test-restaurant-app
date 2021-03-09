package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.model.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ReviewMapper extends ResourceEntityMapper<ReviewResource, Review> {
}

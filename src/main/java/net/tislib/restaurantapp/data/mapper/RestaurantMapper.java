package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {ReviewMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper extends ResourceEntityMapper<RestaurantResource, Restaurant> {

    @Mapping(source = "reviewStats.ratingAverage", target = "rating")
    @Mapping(source = "reviewStats.highestRatedReview", target = "highestRatedReview")
    @Mapping(source = "reviewStats.lowestRatedReview", target = "lowestRatedReview")
    @Mapping(source = "reviewStats.ratingCount", target = "ratingCount")
    @Override
    RestaurantResource to(Restaurant entity);

    @Mapping(target = "owner", ignore = true)
    @Override
    void mapFrom(@MappingTarget Restaurant entity, RestaurantResource resource);

}

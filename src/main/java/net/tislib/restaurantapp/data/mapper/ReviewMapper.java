package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, RestaurantMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper extends ResourceEntityMapper<ReviewResource, Review> {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Override
    void mapFrom(@MappingTarget Review entity, ReviewResource resource);

}

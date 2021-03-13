package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper extends ResourceEntityMapper<UserResource, User> {

    @Mapping(target = UserResource.Fields.password, ignore = true)
    @Override
    UserResource to(User entity);

    @Mapping(target = UserResource.Fields.password, ignore = true)
    @Override
    void mapTo(@MappingTarget UserResource resource, User entity);

}

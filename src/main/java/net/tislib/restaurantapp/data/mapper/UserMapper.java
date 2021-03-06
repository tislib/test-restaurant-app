package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResource from(User user);

    User to(UserResource user);

}

package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;

public interface UserService {
    UserResource create(UserResource resource);

    PageContainer<UserResource> list();

    UserResource get(Long id);

    UserResource update(Long id, UserResource resource);

    UserResource delete(Long id);
}

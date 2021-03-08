package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import org.springframework.data.domain.Page;

public interface UserService {
    PageContainer<UserResource> list();

    UserResource get(Long id);

    UserResource update(Long id, UserResource userResource);
}

package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResource create(UserResource resource);

    PageContainer<UserResource> list(Pageable pageable);

    UserResource get(Long id);

    UserResource update(Long id, UserResource resource);

    UserResource delete(Long id);
}

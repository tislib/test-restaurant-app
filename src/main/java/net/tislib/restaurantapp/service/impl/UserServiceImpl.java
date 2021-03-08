package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public PageContainer<UserResource> list() {
        return null;
    }

    @Override
    public UserResource get(Long id) {
        return null;
    }

    @Override
    public UserResource update(Long id, UserResource userResource) {
        return null;
    }
}

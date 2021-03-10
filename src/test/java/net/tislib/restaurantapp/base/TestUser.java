package net.tislib.restaurantapp.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TestUser {
    ADMIN_USER("admin@testapp.com", "admin123");

    private final String email;
    private final String password;
}

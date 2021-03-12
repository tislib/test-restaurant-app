package net.tislib.restaurantapp.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TestUser {
    REGULAR_USER_1("user@testapp.com", "admin123"),
    REGULAR_OWNER_1("owner@testapp.com", "admin123"),
    REGULAR_OWNER_2("owner2@testapp.com", "admin123"),
    ADMIN_USER("admin@testapp.com", "admin123");

    private final String email;
    private final String password;
}

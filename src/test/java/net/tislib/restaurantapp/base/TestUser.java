package net.tislib.restaurantapp.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TestUser {
    REGULAR_USER_1("user@testapp.com", Constants.ADMIN_123),
    OWNER_USER_1("owner@testapp.com", Constants.ADMIN_123),
    OWNER_USER_2("owner2@testapp.com", Constants.ADMIN_123),
    ADMIN_USER("admin@testapp.com", Constants.ADMIN_123);

    private final String email;
    private final String password;

    private static class Constants {
        public static final String ADMIN_123 = "admin123";
    }
}

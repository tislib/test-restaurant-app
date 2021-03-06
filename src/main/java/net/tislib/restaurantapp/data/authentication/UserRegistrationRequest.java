package net.tislib.restaurantapp.data.authentication;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String email;
    private String password;
}

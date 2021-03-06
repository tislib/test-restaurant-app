package net.tislib.restaurantapp.data.authentication;

import lombok.Data;

@Data
public class TokenCreateRequest {

    private String email;
    private String password;

}

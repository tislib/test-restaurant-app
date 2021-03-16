package net.tislib.restaurantapp.data.authentication;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class TokenCreateRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

}

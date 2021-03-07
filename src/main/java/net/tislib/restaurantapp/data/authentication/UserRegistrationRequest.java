package net.tislib.restaurantapp.data.authentication;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationRequest {

    @Email
    private String email;

    @NotBlank
    @Length(min = 6, max = 255)
    private String password;
}

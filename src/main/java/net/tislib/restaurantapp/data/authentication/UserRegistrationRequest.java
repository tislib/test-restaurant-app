package net.tislib.restaurantapp.data.authentication;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRegistrationRequest {

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Length(min = 6, max = 255)
    private String password;

    @Length(min = 1, max = 64)
    @NotBlank
    private String fullName;
}

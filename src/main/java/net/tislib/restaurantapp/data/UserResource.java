package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import net.tislib.restaurantapp.model.UserRole;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants
public class UserResource extends Resource<UserResource> {

    @Schema(accessMode = READ_ONLY)
    private Long id;

    @NotBlank
    @Email
    @Schema(example = "talehsmail@gmail.com")
    private String email;

    @NotNull
    @Schema(example = "OWNER",
            description = "user role: REGULAR for ordinary users; OWNER for restaurant owners; ADMIN for administrators")
    private UserRole role;

    @Schema(accessMode = WRITE_ONLY, example = "pass123")
    @Length(min = 6, max = 255)
    private String password;
}

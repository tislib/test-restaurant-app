package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Links;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResource extends Resource<UserResource> {

    private String email;

    private UserRole role;

    @Override
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Links getLinks() {
        return super.getLinks();
    }

}
package net.tislib.restaurantapp.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResource extends RepresentationModel<UserResource> {

    private String email;

}
package net.tislib.restaurantapp.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReviewResource extends RepresentationModel<UserResource> {
}

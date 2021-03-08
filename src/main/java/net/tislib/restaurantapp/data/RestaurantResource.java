package net.tislib.restaurantapp.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RestaurantResource extends Resource<RestaurantResource> {

    private Long id;

    private String name;

}

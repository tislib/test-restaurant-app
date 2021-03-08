package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

public abstract class Resource<T extends Resource<? extends T>> extends RepresentationModel<T> {

    @Override
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Links getLinks() {
        return super.getLinks();
    }
}

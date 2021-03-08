package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tislib.restaurantapp.model.User;

import java.math.BigDecimal;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
public class RestaurantResource extends Resource<RestaurantResource> {

    private Long id;

    @Schema(description = "restaurant name")
    private String name;

    @Schema(description = "restaurant owner")
    private User owner;

    @Schema(accessMode = READ_ONLY, description = "average rating for all reviews")
    private BigDecimal rating;

    @Schema(accessMode = READ_ONLY)
    private ReviewResource highestRatedReview;

    @Schema(accessMode = READ_ONLY)
    private ReviewResource lowestRatedReview;

    @Schema(accessMode = READ_ONLY)
    private List<ReviewResource> lastReviews;

}

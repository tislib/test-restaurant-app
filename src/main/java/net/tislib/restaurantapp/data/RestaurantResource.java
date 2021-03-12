package net.tislib.restaurantapp.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Data
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants
public class RestaurantResource extends Resource<RestaurantResource> {

    @Schema(accessMode = READ_ONLY)
    private Long id;

    @Schema(description = "restaurant name")
    @NotBlank
    private String name;

    @Schema(description = "restaurant owner")
    @NotNull
    private UserResource owner;

    @Schema(accessMode = READ_ONLY, description = "average rating for all reviews")
    private BigDecimal rating;

    @Schema(accessMode = READ_ONLY, description = "average rating for all reviews")
    private int ratingCount;

    @Schema(accessMode = READ_ONLY)
    private ReviewResource highestRatedReview;

    @Schema(accessMode = READ_ONLY)
    private ReviewResource lowestRatedReview;

}

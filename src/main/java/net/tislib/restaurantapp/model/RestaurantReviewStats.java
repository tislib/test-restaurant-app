package net.tislib.restaurantapp.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@Entity
@FieldNameConstants
public class RestaurantReviewStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Restaurant restaurant;

    private Long ratingAverage;

    private Long ratingSum;

    private Integer ratingCount;

    @ManyToOne
    private Review highestRatedReview;

    @ManyToOne
    private Review lowestRatedReview;

}

package net.tislib.restaurantapp.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.math.BigDecimal;

@Data
@Entity
@FieldNameConstants
public class RestaurantReviewStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @ToString.Exclude
    private Restaurant restaurant;

    private BigDecimal ratingAverage = BigDecimal.ZERO;

    private long ratingSum;

    private int ratingCount;

    @ManyToOne
    private Review highestRatedReview;

    @ManyToOne
    private Review lowestRatedReview;

    @Version
    private long version;

}

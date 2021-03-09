package net.tislib.restaurantapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsRepository;
import net.tislib.restaurantapp.model.repository.ReviewRepository;
import net.tislib.restaurantapp.service.RestaurantReviewStatsService;
import org.hibernate.StaleStateException;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantReviewStatsServiceImpl implements RestaurantReviewStatsService {

    public static final int OPTIMISTIC_LOCK_MAX_RETRY_THRESHOLD = 10;
    private final RestaurantReviewStatsRepository repository;
    private final ReviewRepository reviewRepository;

    @Override
    public void computeReview(short previousStarCount, Review review, boolean reviewDeleted) {
        RestaurantReviewStats reviewStats = repository.findByRestaurantId(review.getRestaurant().getId())
                .orElseThrow();

        for (int i = 0; i < OPTIMISTIC_LOCK_MAX_RETRY_THRESHOLD; i++) {
            try {
                calculateState(reviewStats, previousStarCount, review, reviewDeleted);

                repository.exclusiveUpdateReviewStats(reviewStats);

                review.setComputed(true);
                reviewRepository.save(review);

                return;
            } catch (OptimisticLockException | StaleStateException e) {
                log.debug("optimistic fail retryable exception: {}", e.getMessage());
            }
        }
    }

    private void calculateState(RestaurantReviewStats reviewStats, short previousStarCount, Review review, boolean reviewDeleted) {
        short starChangeCount = (short) (review.getStarCount() - previousStarCount);

        reviewStats.setRatingSum(reviewStats.getRatingSum() + starChangeCount);
        if (!reviewDeleted) {
            reviewStats.setRatingCount(reviewStats.getRatingCount() + 1);
        } else {
            reviewStats.setRatingCount(reviewStats.getRatingCount() - 1);
        }

        reviewStats.setRatingAverage(BigDecimal.valueOf(reviewStats.getRatingSum())
                .divide(BigDecimal.valueOf(reviewStats.getRatingCount()), 2, RoundingMode.FLOOR));

        if (reviewStats.getLowestRatedReview() == null || reviewStats.getLowestRatedReview().getStarCount() > review.getStarCount()) {
            reviewStats.setLowestRatedReview(review);
        }

        if (reviewStats.getHighestRatedReview() == null || reviewStats.getHighestRatedReview().getStarCount() < review.getStarCount()) {
            reviewStats.setHighestRatedReview(review);
        }

        if (reviewDeleted) {
            // if we are removing lowest rated review
            if (Objects.equals(reviewStats.getLowestRatedReview().getId(), review.getId())) {
                Review lowestRatedRecord = reviewRepository.findFirstByRestaurantIdOrderByStarCountAsc(reviewStats.getRestaurant().getId())
                        .orElse(null);

                reviewStats.setLowestRatedReview(lowestRatedRecord);
            }

            if (Objects.equals(reviewStats.getHighestRatedReview().getId(), review.getId())) {
                Review lowestRatedRecord = reviewRepository.findFirstByRestaurantIdOrderByStarCountDesc(reviewStats.getRestaurant().getId())
                        .orElse(null);

                reviewStats.setLowestRatedReview(lowestRatedRecord);
            }
        }
    }
}

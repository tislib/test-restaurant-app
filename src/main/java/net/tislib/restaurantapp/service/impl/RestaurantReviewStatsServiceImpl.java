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

import javax.annotation.PostConstruct;
import javax.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RestaurantReviewStatsServiceImpl implements RestaurantReviewStatsService {

    public static final int OPTIMISTIC_LOCK_MAX_RETRY_THRESHOLD = 10;
    private final RestaurantReviewStatsRepository repository;
    private final ReviewRepository reviewRepository;

    @PostConstruct
    public void init() {
        log.trace("recompute reviews");

        /*
            recomputing not computed reviews
            this code block is used to tolerate application crash
            if computeReview:46 method not able to compute application review,
            after application restart all not computed reviews will be computed
         */
        recomputeNotComputedReviews();
    }

    private void recomputeNotComputedReviews() {
        // get list of not computed reviews ( restaurant stats not computed )
        List<Review> notComputedReviews = reviewRepository.findByComputed(false);

        // recompute reviews for each item
        notComputedReviews.forEach(review -> computeReview((short) 0, review, 1));
    }

    @Override
    public void computeReview(short previousStarCount, Review review, int countDiff) {
        /*
         * previousStarCount is previous review count before operation, this is used to indicate review change
         * review is the review which will be computed
         * countDiff how reviews count will be changed
         *      if countDiff > 0 it means adding new record added
         *      if countDiff == 0 it means record is updated
         *      if countDiff < 0 it means record is deleted
         */
        RestaurantReviewStats reviewStats = repository.findByRestaurantId(review.getRestaurant().getId())
                .orElseThrow();

        log.debug("computing review for reviewId: {}; restaurantId: {}, countDiff: {}",
                review.getId(),
                reviewStats.getRestaurant().getId(),
                countDiff);

        /*
            optimistic lock is used for updating restaurant review stats exclusively
            Reasons for using optimistic lock and exclusive operations:
            If we have scaled application and have parallel updates, it will cause data loss and miss calculations
            To prevent concurrency problems we need to add some locking mechanism
            (pessimistic lock, optimistic lock in database side or third party locking system for distributed lock (redis redlock, etc.))
            For specifically this case optimistic lock is more useful than pessimistic lock,
            if we use pessimistic lock instead we will have blocked a lot of blocked reviews and it will cause live locks
            review count is much more higher than restaurant count it means that pessimistic lock in our case is wrong way to use.
         */
        for (int i = 0; i < OPTIMISTIC_LOCK_MAX_RETRY_THRESHOLD; i++) {
            try {
                log.debug("calculating reviewStats for review: {}; try:{}", review, i);
                // calculate new review statistic by applying review
                calculateStats(reviewStats, previousStarCount, review, countDiff);

                log.debug("calculating saving new review stats: {}; try:{}", reviewStats, i);

                // update review stats exclusively
                repository.exclusiveUpdateReviewStats(reviewStats);

                // set review computed if review is new review
                if (countDiff >= 0) {
                    review.setComputed(true);

                    log.debug("saving review as computed: reviewId: {}; try:{}", review.getId(), i);
                    reviewRepository.save(review);
                }

                return;
            } catch (OptimisticLockException | StaleStateException e) {
                log.debug("optimistic fail retryable exception: {}", e.getMessage());
            }
        }
    }

    private void calculateStats(RestaurantReviewStats reviewStats, short previousStarCount, Review review, int countDiff) {
        log.trace("calculating review stats");
        short starChangeCount = (short) (review.getStarCount() - previousStarCount);
        log.debug("changed star count is {}", starChangeCount);

        reviewStats.setRatingSum(reviewStats.getRatingSum() + starChangeCount);
        reviewStats.setRatingCount(reviewStats.getRatingCount() + countDiff);

        reviewStats.setRatingAverage(BigDecimal.valueOf(reviewStats.getRatingSum())
                .divide(BigDecimal.valueOf(reviewStats.getRatingCount()), 2, RoundingMode.FLOOR));

        /*
        if countDiff > 0 (new review is added) check if new review is highest or lowest rated review and update stats
        else find highest and lowest rated reviews from database (with database aggregation)
        database aggregation causes performance problems but if we consider update or delete is much fewer than inserts we can use database aggregation for them
        if record is deleted we need to find previous lowest or previous highest record
         */
        if (countDiff > 0) {
            if (reviewStats.getLowestRatedReview() == null || reviewStats.getLowestRatedReview().getStarCount() > review.getStarCount()) {
                reviewStats.setLowestRatedReview(review);
                log.debug("lowest rated review is set to {}", review.getId());
            }

            if (reviewStats.getHighestRatedReview() == null || reviewStats.getHighestRatedReview().getStarCount() < review.getStarCount()) {
                reviewStats.setHighestRatedReview(review);
                log.debug("highest rated review is set to {}", review.getId());
            }
        } else {
            // if we are removing lowest rated review
            reviewStats.setLowestRatedReview(
                    reviewRepository.findFirstByRestaurantIdOrderByStarCountAsc(reviewStats.getRestaurant().getId())
                            .orElse(null)
            );

            reviewStats.setHighestRatedReview(
                    reviewRepository.findFirstByRestaurantIdOrderByStarCountDesc(reviewStats.getRestaurant().getId())
                            .orElse(null)
            );
        }
    }
}

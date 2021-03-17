package net.tislib.restaurantapp.model.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsExtendedRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

@Repository
@Log4j2
@RequiredArgsConstructor
public class RestaurantReviewStatsExtendedRepositoryImpl implements RestaurantReviewStatsExtendedRepository {

    private final EntityManager entityManager;


    @Override
    @Transactional
    public void exclusiveUpdateReviewStats(RestaurantReviewStats reviewStatsUpdate) {
        RestaurantReviewStats reviewStats = entityManager.find(RestaurantReviewStats.class,
                reviewStatsUpdate.getId(),
                LockModeType.OPTIMISTIC);

        reviewStats.setRatingAverage(reviewStatsUpdate.getRatingAverage());
        reviewStats.setRatingCount(reviewStatsUpdate.getRatingCount());
        reviewStats.setRatingSum(reviewStatsUpdate.getRatingSum());
        reviewStats.setLowestRatedReview(reviewStatsUpdate.getLowestRatedReview());
        reviewStats.setHighestRatedReview(reviewStatsUpdate.getHighestRatedReview());

        reviewStats.setVersion(reviewStats.getVersion() + 1);

        entityManager.persist(reviewStats);
    }
}

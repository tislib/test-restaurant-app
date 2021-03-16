package net.tislib.restaurantapp.model.repository;

import net.tislib.restaurantapp.model.RestaurantReviewStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantReviewStatsRepository extends JpaRepository<RestaurantReviewStats, Long>, RestaurantReviewStatsExtendedRepository {

    Optional<RestaurantReviewStats> findByRestaurantId(long restaurantId);

}

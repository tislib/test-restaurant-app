package net.tislib.restaurantapp.model.repository;

import net.tislib.restaurantapp.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findAllByOrderByReviewStatsRatingAverage(Pageable pageable);
}

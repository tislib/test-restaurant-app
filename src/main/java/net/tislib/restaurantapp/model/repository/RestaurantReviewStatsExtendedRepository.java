package net.tislib.restaurantapp.model.repository;

import net.tislib.restaurantapp.model.RestaurantReviewStats;

public interface RestaurantReviewStatsExtendedRepository {

    void exclusiveUpdateReviewStats(RestaurantReviewStats reviewStats);
}

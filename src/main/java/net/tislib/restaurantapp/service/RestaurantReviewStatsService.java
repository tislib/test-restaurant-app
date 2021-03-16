package net.tislib.restaurantapp.service;

import net.tislib.restaurantapp.model.Review;

public interface RestaurantReviewStatsService {
    void computeReview(short previousStarCount, Review review, int countDiff);
}

package net.tislib.restaurantapp.service.impl;

import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsRepository;
import net.tislib.restaurantapp.model.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD")
class RestaurantReviewStatsServiceImplTest {

    private static final long ONE = 1L;

    @InjectMocks
    private RestaurantReviewStatsServiceImpl reviewStatsService;

    @Mock
    private RestaurantReviewStatsRepository repository;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    void callInitAndExpectReviewsCalculatingAsExpected() {
        //Arrange
        Restaurant restaurant = prepareRestaurant();

        Review review = prepareReview(restaurant);

        List<Review> reviews = new ArrayList<>();
        reviews.add(review);

        Review expectedReview = prepareExpectedReview(restaurant);

        RestaurantReviewStats reviewStats = prepareRestaurantReviewStats();
        reviewStats.setRestaurant(restaurant);

        Optional<RestaurantReviewStats> optionalRestaurantReviewStats = Optional.of(reviewStats);
        // Act
        when(reviewRepository.findByComputed(false)).thenReturn(reviews);
        when(repository.findByRestaurantId(ONE)).thenReturn(optionalRestaurantReviewStats);
        doNothing().when(repository).exclusiveUpdateReviewStats(reviewStats);

        reviewStatsService.init();

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewArgumentCaptor.capture());

        // Assert
        verify(reviewRepository).findByComputed(false);
        verify(reviewRepository).save(review);
        verify(repository).findByRestaurantId(ONE);
        verify(repository).exclusiveUpdateReviewStats(reviewStats);

        assertThat(expectedReview.getRestaurant(), equalTo(reviewArgumentCaptor.getValue().getRestaurant()));
        assertThat(expectedReview.getId(), equalTo(reviewArgumentCaptor.getValue().getId()));
        assertThat(expectedReview.getStarCount(), equalTo(reviewArgumentCaptor.getValue().getStarCount()));
        assertThat(expectedReview.isComputed(), equalTo(reviewArgumentCaptor.getValue().isComputed()));
    }

    private RestaurantReviewStats prepareRestaurantReviewStats() {
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();
        reviewStats.setRatingSum(1234);
        reviewStats.setRatingCount(123);

        return reviewStats;
    }

    private Review prepareExpectedReview(Restaurant restaurant) {
        Review expectedReview = new Review();
        expectedReview.setRestaurant(restaurant);
        expectedReview.setId(ONE);
        expectedReview.setStarCount((short) 5);
        expectedReview.setComputed(true);

        return expectedReview;
    }

    private Review prepareReview(Restaurant restaurant) {
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setId(ONE);
        review.setStarCount((short) 5);

        return review;
    }

    private Restaurant prepareRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(ONE);

        return restaurant;
    }

}

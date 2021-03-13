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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantReviewStatsServiceImplTest {

    @InjectMocks
    RestaurantReviewStatsServiceImpl reviewStatsService;

    @Mock
    private RestaurantReviewStatsRepository repository;

    @Mock
    private ReviewRepository reviewRepository;


    @Test
    void test() {
        //Arrange

        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        review.setRestaurant(restaurant);
        review.setId(1L);
        review.setStarCount((short) 5);


        reviews.add(review);
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();

        reviewStats.setRatingSum(1234);
        reviewStats.setRatingCount(123);
        Optional<RestaurantReviewStats> optionalRestaurantReviewStats = Optional.of(reviewStats);

        //Act
        when(reviewRepository.findByComputed(false)).thenReturn(reviews);
        when(repository.findByRestaurantId(1L)).thenReturn(optionalRestaurantReviewStats);
        doNothing().when(repository).exclusiveUpdateReviewStats(reviewStats);


        reviewStatsService.init();

        //Assert
        verify(reviewRepository).findByComputed(false);
        verify(repository).exclusiveUpdateReviewStats(reviewStats);
    }

    @Test
    void test2 ()throws Exception{
        Review review=new Review();
        Restaurant restaurant=new Restaurant();
        review.setRestaurant(restaurant);
        review.setId(1L);
        review.setStarCount((short) 5);
        reviewStatsService.computeReview((short) 10,review,0);
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);

        verify(reviewRepository).save(reviewArgumentCaptor.capture());

    }


}

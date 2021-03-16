package net.tislib.restaurantapp.service.impl;

import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.data.mapper.OwnerReplyMapper;
import net.tislib.restaurantapp.data.mapper.ReviewMapper;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.repository.OwnerReplyRepository;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.ReviewRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import net.tislib.restaurantapp.service.RestaurantReviewStatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    private final static String SAMPLE_REVIEW_TIME = "2021-03-16T10:37:30.00Z";
    private final static Long SAMPLE_ID_ONE = 1L;
    private final static String SAMPLE_COMMENT = "SampleComment";
    private final static short SAMPLE_STAR_COUNT = 5;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository repository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewMapper mapper;

    @Mock
    private OwnerReplyMapper ownerReplyMapper;

    @Mock
    private RestaurantReviewStatsService reviewStatsService;

    @Mock
    private OwnerReplyRepository ownerReplyRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void create() {
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();
        reviewStats.setId(SAMPLE_ID_ONE);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("SampleRestaurantName");
        restaurant.setId(SAMPLE_ID_ONE);

        Review review = prepareReview();
        Optional<Review> optionalReview = Optional.of(review);
        ReviewResource reviewResource = prepareReviewResource();

        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        Review expectedReview = prepareReview();

        restaurant.setReviewStats(reviewStats);

        when(restaurantRepository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        when(mapper.from(reviewResource)).thenReturn(review);
        when(repository.save(review)).thenReturn(review);
        doNothing().when(reviewStatsService).computeReview((short) 0, review, 1);
        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);

        reviewService.create(SAMPLE_ID_ONE, reviewResource);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(repository).save(reviewArgumentCaptor.capture());


        verify(restaurantRepository).findById(SAMPLE_ID_ONE);
        verify(mapper).to(review);
        verify(reviewStatsService).computeReview((short) 0, review, 1);

        assertThat(expectedReview.getId(), is(reviewArgumentCaptor.getValue().getId()));

    }

    @Test
    void list() {
    }

    @Test
    void get() {
        //Arrange
        Review review = prepareReview();

        ReviewResource reviewResource = prepareReviewResource();
        Optional<Review> optionalReview = Optional.of(review);

        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);
        when(mapper.to(review)).thenReturn(reviewResource);
        //Act
        ReviewResource actualResult = reviewService.get(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        //Assert
        verify(repository).findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        verify(mapper).to(review);

        assertThat(actualResult.getId(), is(SAMPLE_ID_ONE));
        assertThat(actualResult.getReviewTime(), is(Instant.parse(SAMPLE_REVIEW_TIME)));
        assertThat(actualResult.getComment(), is(SAMPLE_COMMENT));
        assertThat(actualResult.getStarCount(), is(SAMPLE_STAR_COUNT));
    }

    @Test
    void update() {
    }

    @Test
    void updateOwnerReply() {

    }

    @Test
    void delete() {
        //Arrange
        Review review = prepareReview();
        Optional<Review> optionalReview = Optional.of(review);

        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);
        doNothing().when(repository).delete(review);
        doNothing().when(reviewStatsService).computeReview(SAMPLE_STAR_COUNT, review, -1);
        //Act
        reviewService.delete(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        //Assert
        verify(repository).findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        verify(repository).delete(review);
        verify(reviewStatsService).computeReview(SAMPLE_STAR_COUNT, review, -1);
    }

    private Review prepareReview() {
        Review review = new Review();
        review.setReviewTime(Instant.parse(SAMPLE_REVIEW_TIME));
        review.setId(SAMPLE_ID_ONE);
        review.setComment(SAMPLE_COMMENT);
        review.setStarCount(SAMPLE_STAR_COUNT);
        return review;
    }

    private ReviewResource prepareReviewResource() {
        ReviewResource reviewResource = new ReviewResource();
        reviewResource.setReviewTime(Instant.parse(SAMPLE_REVIEW_TIME));
        reviewResource.setId(SAMPLE_ID_ONE);
        reviewResource.setComment(SAMPLE_COMMENT);
        reviewResource.setStarCount(SAMPLE_STAR_COUNT);
        return reviewResource;
    }
}
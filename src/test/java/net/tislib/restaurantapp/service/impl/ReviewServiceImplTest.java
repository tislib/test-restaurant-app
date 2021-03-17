package net.tislib.restaurantapp.service.impl;

import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.ReviewResource;
import net.tislib.restaurantapp.data.mapper.OwnerReplyMapper;
import net.tislib.restaurantapp.data.mapper.ReviewMapper;
import net.tislib.restaurantapp.model.OwnerReply;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.Review;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.UserRole;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD")
class ReviewServiceImplTest {

    private static final String SAMPLE_REVIEW_TIME = "2021-03-16T10:37:30.00Z";
    private static final Long SAMPLE_ID_ONE = 1L;
    private static final String SAMPLE_COMMENT = "SampleComment";
    private static final short SAMPLE_STAR_COUNT = 5;

    private static final String SAMPLE_RESTAURANT_NAME = "SampleRestaurantName";

    private static final String SAMPLE_EMAIL = "SampleEmail";
    private static final String SAMPLE_FULL_NAME = "SampleFullName";
    private static final String SAMPLE_PASSWORD = "SamplePassword";

    @InjectMocks
    private ReviewServiceImpl reviewService;

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
    void callReviewAndExpectReviewEntityCreatedSuccessfully() {
        //Arrange
        RestaurantReviewStats reviewStats = new RestaurantReviewStats();
        reviewStats.setId(SAMPLE_ID_ONE);

        Restaurant restaurant = prepareRestaurant();
        restaurant.setReviewStats(reviewStats);

        Review review = prepareReview();
        Optional<Review> optionalReview = Optional.of(review);
        ReviewResource reviewResource = prepareReviewResource();

        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        Review expectedReview = prepareReview();

        when(restaurantRepository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        when(mapper.from(reviewResource)).thenReturn(review);
        when(repository.save(review)).thenAnswer(invocation -> {
            review.setId(SAMPLE_ID_ONE);
            return review;
        });
        when(mapper.to(review)).thenReturn(reviewResource);
        doNothing().when(reviewStatsService).computeReview((short) 0, review, 1);
        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);
        //Act
        reviewService.create(SAMPLE_ID_ONE, reviewResource);
        //Assert
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        verify(repository).save(reviewArgumentCaptor.capture());

        verify(restaurantRepository).findById(SAMPLE_ID_ONE);
        verify(mapper).to(review);
        verify(reviewStatsService).computeReview((short) 0, review, 1);

        assertThat(expectedReview.getId(), is(reviewArgumentCaptor.getValue().getId()));
        assertThat(expectedReview.getReviewTime(), is(notNullValue()));
        assertThat(expectedReview.getComment(), is(reviewArgumentCaptor.getValue().getComment()));
        assertThat(expectedReview.getStarCount(), is(reviewArgumentCaptor.getValue().getStarCount()));

    }

    @Test
    void callGetAndExpectEntityFoundedSuccessfully() {
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
    void callUpdateAndExpectReviewEntityUpdatedSuccessfully() {
        //Arrange
        Restaurant restaurant = prepareRestaurant();
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        Review review = prepareReview();
        Optional<Review> optionalReview = Optional.of(review);

        ReviewResource reviewResource = prepareReviewResource();

        when(restaurantRepository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);
        doNothing().when(mapper).mapFrom(review, reviewResource);
        doNothing().when(reviewStatsService).computeReview((short) 5, review, 0);
        when(mapper.to(review)).thenReturn(reviewResource);
        //Act
        reviewService.update(SAMPLE_ID_ONE, SAMPLE_ID_ONE, reviewResource);

        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        //Assert
        verify(repository).save(reviewArgumentCaptor.capture());

        verify(restaurantRepository).findById(SAMPLE_ID_ONE);
        verify(repository, times(2)).findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        verify(mapper).mapFrom(review, reviewResource);
        verify(reviewStatsService).computeReview((short) 5, review, 0);
        verify(mapper).to(review);

        assertThat(review.getStarCount(), is(reviewArgumentCaptor.getValue().getStarCount()));
        assertThat(review.getRestaurant(), is(reviewArgumentCaptor.getValue().getRestaurant()));
        assertThat(review.getReviewTime(), is(reviewArgumentCaptor.getValue().getReviewTime()));
        assertThat(review.getId(), is(reviewArgumentCaptor.getValue().getId()));
        assertThat(review.getComment(), is(reviewArgumentCaptor.getValue().getComment()));
    }

    @Test
    void callUpdateOwnerReplyAndExpectOwnerReplyEntityUpdatedSuccessfully() {
        OwnerReplyResource ownerReplyResource = prepareOwnerReply();

        Review review = prepareReview();
        Optional<Review> optionalReview = Optional.of(review);

        User user = prepareUser();

        when(repository.findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE)).thenReturn(optionalReview);
        when(authenticationService.getCurrentUser()).thenReturn(user);
        doNothing().when(ownerReplyMapper).mapFrom(any(OwnerReply.class), any(OwnerReplyResource.class));
        when(ownerReplyMapper.to(any(OwnerReply.class))).thenReturn(ownerReplyResource);

        OwnerReplyResource updatedOwnerReplyResource =
                reviewService.updateOwnerReply(SAMPLE_ID_ONE, SAMPLE_ID_ONE, ownerReplyResource);

        ArgumentCaptor<OwnerReply> ownerReplyArgumentCaptor = ArgumentCaptor.forClass(OwnerReply.class);
        verify(ownerReplyRepository).save(ownerReplyArgumentCaptor.capture());

        verify(repository).findByRestaurantIdAndId(SAMPLE_ID_ONE, SAMPLE_ID_ONE);
        verify(authenticationService).getCurrentUser();
        verify(ownerReplyMapper).mapFrom(any(OwnerReply.class), any(OwnerReplyResource.class));
        verify(ownerReplyMapper).to(any(OwnerReply.class));

        assertThat(updatedOwnerReplyResource.getComment(), is(ownerReplyArgumentCaptor.getValue().getComment()));
    }

    @Test
    void callDeleteAndExpectReviewEntityDeletedSuccessfully() {
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
        OwnerReply ownerReply = new OwnerReply();
        ownerReply.setComment(SAMPLE_COMMENT);
        ownerReply.setId(SAMPLE_ID_ONE);
        Review review = new Review();
        review.setReviewTime(Instant.parse(SAMPLE_REVIEW_TIME));
        review.setId(SAMPLE_ID_ONE);
        review.setComment(SAMPLE_COMMENT);
        review.setStarCount(SAMPLE_STAR_COUNT);
        review.setOwnerReply(ownerReply);

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

    private Restaurant prepareRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(SAMPLE_RESTAURANT_NAME);
        restaurant.setId(SAMPLE_ID_ONE);

        return restaurant;
    }

    private OwnerReplyResource prepareOwnerReply() {
        OwnerReplyResource ownerReplyResource = new OwnerReplyResource();
        ownerReplyResource.setReplyTime(Instant.now());
        ownerReplyResource.setComment(SAMPLE_COMMENT);

        return ownerReplyResource;
    }

    private User prepareUser() {
        User user = new User();
        user.setId(SAMPLE_ID_ONE);
        user.setEmail(SAMPLE_EMAIL);
        user.setFullName(SAMPLE_FULL_NAME);
        user.setPassword(SAMPLE_PASSWORD);
        user.setRole(UserRole.REGULAR);

        return user;
    }

}

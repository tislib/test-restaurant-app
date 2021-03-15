package net.tislib.restaurantapp.service.impl;

import net.tislib.restaurantapp.data.RestaurantResource;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.mapper.RestaurantMapper;
import net.tislib.restaurantapp.model.Restaurant;
import net.tislib.restaurantapp.model.RestaurantReviewStats;
import net.tislib.restaurantapp.model.User;
import net.tislib.restaurantapp.model.UserRole;
import net.tislib.restaurantapp.model.repository.RestaurantRepository;
import net.tislib.restaurantapp.model.repository.RestaurantReviewStatsRepository;
import net.tislib.restaurantapp.model.repository.UserRepository;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    private static final String ENTITY_NOT_FOUND_EXCEPTION_ERROR_MESSAGE = "restaurant not found with id: 6";
    private static final String SAMPLE_RESTAURANT_NAME = "SampleRestaurantName";
    private static final Long SAMPLE_ID_ONE = 1L;
    private static final Long SAMPLE_ID_SIX = 6L;


    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private RestaurantRepository repository;

    @Mock
    private RestaurantMapper mapper;

    @Mock
    private RestaurantReviewStatsRepository reviewStatsRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void create() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(SAMPLE_ID_ONE);
        User user = new User();
        user.setRole(UserRole.REGULAR);

        UserResource userResource = new UserResource();
        userResource.setId(SAMPLE_ID_ONE);

        RestaurantResource restaurantResource = new RestaurantResource();
        restaurantResource.setOwner(userResource);
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        when(mapper.from(restaurantResource)).thenReturn(restaurant);
        when(authenticationService.getCurrentUser()).thenReturn(user);

        when(repository.findById(null)).thenReturn(optionalRestaurant);
        when(mapper.to(restaurant)).thenReturn(restaurantResource);

        restaurantService.create(restaurantResource);

        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(repository).save(restaurantArgumentCaptor.capture());

        ArgumentCaptor<RestaurantReviewStats> restaurantReviewStatsArgumentCaptor = ArgumentCaptor.forClass(RestaurantReviewStats.class);
        verify(reviewStatsRepository).save(restaurantReviewStatsArgumentCaptor.capture());

        //Should complete feature

    }

    @Test
    void list() {
    }

    @Test
    void get() {
        //Arrange
        Restaurant restaurant = new Restaurant();
        UserResource owner = new UserResource();
        owner.setId(SAMPLE_ID_ONE);
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        RestaurantResource restaurantResource = new RestaurantResource();
        restaurantResource.setOwner(owner);
        restaurantResource.setId(SAMPLE_ID_ONE);
        restaurantResource.setName(SAMPLE_RESTAURANT_NAME);

        //Act
        when(repository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        when(mapper.to(restaurant)).thenReturn(restaurantResource);

        RestaurantResource actualResult = restaurantService.get(SAMPLE_ID_ONE);

        //Assert
        verify(repository).findById(SAMPLE_ID_ONE);

        assertThat(actualResult.getId(), is(SAMPLE_ID_ONE));
        assertThat(actualResult.getName(), is(SAMPLE_RESTAURANT_NAME));
        assertThat(actualResult.getOwner(), is(owner));
    }

    @Test
    void callGetAndExpectEntityNotFoundException() {
        //Arrange & Act & Assert
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> restaurantService.get(SAMPLE_ID_SIX));

        assertThat(exception.getMessage(), is(ENTITY_NOT_FOUND_EXCEPTION_ERROR_MESSAGE));
    }

    @Test
    void update() {
        //Arrange
        UserResource userResource = prepareUserResourceForUpdate();
        RestaurantResource restaurantResource = prepareRestaurantResourceForUpdate(userResource);
        User currentUser = prepareCurrentUserForUpdate();
        Restaurant restaurant = prepareRestraintForUpdate(currentUser);
        Restaurant expectedRestaurant = prepareExpectedRestaurantForUpdate(currentUser);

        Optional<User> currentUserOptional = Optional.of(currentUser);
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        when(repository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        when(mapper.to(restaurant)).thenReturn(restaurantResource);
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        doNothing().when(mapper).mapFrom(restaurant, restaurantResource);
        when(userRepository.findById(SAMPLE_ID_ONE)).thenReturn(currentUserOptional);
        //Act
        restaurantService.update(SAMPLE_ID_ONE, restaurantResource);

        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(repository).save(restaurantArgumentCaptor.capture());

        verify(repository, times(2)).findById(SAMPLE_ID_ONE);
        verify(mapper, times(2)).to(restaurant);
        verify(authenticationService).getCurrentUser();
        verify(mapper).mapFrom(restaurant, restaurantResource);
        verify(userRepository).findById(SAMPLE_ID_ONE);

        assertThat(expectedRestaurant.getId(), is(restaurantArgumentCaptor.getValue().getId()));
        assertThat(expectedRestaurant.getName(), is(restaurantArgumentCaptor.getValue().getName()));
        assertThat(expectedRestaurant.getOwner(), is(restaurantArgumentCaptor.getValue().getOwner()));
    }

    @Test
    void delete() {
        //Arrange
        Restaurant restaurant = new Restaurant();
        Optional<Restaurant> optionalRestaurant = Optional.of(restaurant);

        doNothing().when(repository).delete(restaurant);
        when(repository.findById(SAMPLE_ID_ONE)).thenReturn(optionalRestaurant);
        //Act
        restaurantService.delete(SAMPLE_ID_ONE);
        //Assert
        verify(repository).findById(SAMPLE_ID_ONE);
        verify(repository).delete(restaurant);
    }

    private User prepareCurrentUserForUpdate() {
        User currentUser = new User();
        currentUser.setRole(UserRole.REGULAR);

        return currentUser;
    }

    private RestaurantResource prepareRestaurantResourceForUpdate(UserResource userResource) {
        RestaurantResource restaurantResource = new RestaurantResource();
        restaurantResource.setOwner(userResource);

        return restaurantResource;
    }

    private UserResource prepareUserResourceForUpdate() {
        UserResource userResource = new UserResource();
        userResource.setId(SAMPLE_ID_ONE);

        return userResource;
    }

    private Restaurant prepareRestraintForUpdate(User currentUser) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(SAMPLE_RESTAURANT_NAME);
        restaurant.setId(SAMPLE_ID_ONE);
        restaurant.setOwner(currentUser);

        return restaurant;
    }

    private Restaurant prepareExpectedRestaurantForUpdate(User currentUser) {
        Restaurant expectedRestaurant = new Restaurant();
        expectedRestaurant.setId(SAMPLE_ID_ONE);
        expectedRestaurant.setName(SAMPLE_RESTAURANT_NAME);
        expectedRestaurant.setOwner(currentUser);

        return expectedRestaurant;
    }

}

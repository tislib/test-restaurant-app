package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.tislib.restaurantapp.constant.ApiConstants.API_RESTAURANTS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerIT extends BaseIntegrationTest {

    public static final String RESTAURANT_1_NAME = "restaurant1";
    public static final int RESTAURANT_ID_1 = 101;
    public static final int RESTAURANT_ID_2 = 102;
    public static final int RESTAURANT_ID_3 = 103;

    @Test
    @SuppressWarnings("PMD")
    public void createRestaurantAndGet200Success() throws Exception {
        // arrange
        auth(TestUser.OWNER_USER_1);

        RestaurantResource resource = new RestaurantResource();
        resource.setName(RESTAURANT_1_NAME);
        resource.setOwner(currentUser);

        // act & assert
        mockMvc.perform(post(API_RESTAURANTS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isCreated()
                ))
                .andExpect(jsonPath("$.name").value(RESTAURANT_1_NAME));
    }

    @Test
    @SuppressWarnings("PMD")
    public void createRestaurantByRegularUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.REGULAR_USER_1);

        RestaurantResource resource = new RestaurantResource();
        resource.setName(RESTAURANT_1_NAME);
        resource.setOwner(currentUser);

        mockMvc.perform(post(API_RESTAURANTS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteRestaurantByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(delete(API_RESTAURANTS + PATH_ID, RESTAURANT_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isNoContent()
                ));

        mockMvc.perform(get(API_RESTAURANTS + PATH_ID, RESTAURANT_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isNotFound()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void getRestaurantByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(get(API_RESTAURANTS + PATH_ID, RESTAURANT_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath(RestaurantResource.Fields.name).value(RESTAURANT_1_NAME)
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void updateRestaurantByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        RestaurantResource resource = new RestaurantResource();

        resource.setOwner(currentUser);
        resource.setName("new-restaurant-name-1");

        mockMvc.perform(put(API_RESTAURANTS + PATH_ID, RESTAURANT_ID_3)
                .content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("name").value(resource.getName())
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteRestaurantByOwnerUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(delete(API_RESTAURANTS + PATH_ID, RESTAURANT_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void listRestaurantsAndCheckResultIsValid() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(get(API_RESTAURANTS))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("content").isArray(),
                        jsonPath("totalElements").isNumber(),
                        jsonPath("totalPages").isNumber()
                ));
    }

}

package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.tislib.restaurantapp.constant.ApiConstants.API_RESTAURANTS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerIT extends BaseIntegrationTest {

    public static final String RESTAURANT_1 = "restaurant-1";

    @Test
    public void createRestaurantAndGet200Success() throws Exception {
        auth(TestUser.REGULAR_OWNER_1);

        RestaurantResource resource = new RestaurantResource();
        resource.setName(RESTAURANT_1);
        resource.setOwner(currentUser);

        mockMvc.perform(post(API_RESTAURANTS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().is2xxSuccessful()
                ))
                .andExpect(jsonPath("$.name").value(RESTAURANT_1));
    }

    @Test
    public void createRestaurantByRegularUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.REGULAR_USER_1);

        RestaurantResource resource = new RestaurantResource();
        resource.setName(RESTAURANT_1);
        resource.setOwner(currentUser);

        mockMvc.perform(post(API_RESTAURANTS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    public void deleteRestaurantByOwnerUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.REGULAR_OWNER_1);

        mockMvc.perform(delete(API_RESTAURANTS + PATH_ID, 1))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

}

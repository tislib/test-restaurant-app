package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.data.RestaurantResource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerIT extends BaseIntegrationTest {

    public static final String STRING = "/api/1.0/restaurants";
    public static final String RESTAURANT_1 = "restaurant-1";

    @Test
    public void createRestaurantAndGet200Success() throws Exception {
        auth(TestUser.ADMIN_USER);

        RestaurantResource resource = new RestaurantResource();
        resource.setName(RESTAURANT_1);
        resource.setOwner(currentUser);

        mockMvc.perform(post(STRING).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().is2xxSuccessful()
                ))
                .andExpect(jsonPath("$.name").value(RESTAURANT_1));
    }

}

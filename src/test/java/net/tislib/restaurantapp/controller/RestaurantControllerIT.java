package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerIT extends BaseIntegrationTest {

    public static final String STRING = "/api/1.0/restaurants";

    @Test
    public void createRestaurantAndGetList() throws Exception {
        mockMvc.perform(post(STRING).content("{\"name\": \"restaurant-1\"}"))
                .andExpect(ResultMatcher.matchAll(
                        status().is2xxSuccessful()
                ));

        mockMvc.perform(get(STRING))
                .andExpect(jsonPath("[0].name").value("restaurant-1"));
    }

}

package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends BaseIntegrationTest {

    public static final String USER_1_FULL_NAME = "testuser1";
    public static final int USER_ID_1 = 105;
    public static final int USER_ID_2 = 106;
    public static final int USER_ID_3 = 107;

    @Test
    @SuppressWarnings("PMD")
    public void createUserAndGet200Success() throws Exception {
        auth(TestUser.ADMIN_USER);

        UserResource resource = prepareUser();

        mockMvc.perform(post(API_USERS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isCreated()
                ))
                .andExpect(jsonPath(UserResource.Fields.fullName).value(USER_1_FULL_NAME));
    }

    @Test
    @SuppressWarnings("PMD")
    public void createUserByRegularUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.REGULAR_USER_1);

        UserResource resource = prepareUser();

        mockMvc.perform(post(API_USERS).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteUserByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(delete(API_USERS + PATH_ID, USER_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isNoContent()
                ));

        mockMvc.perform(get(API_USERS + PATH_ID, USER_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isNotFound()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void getUserByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(get(API_USERS + PATH_ID, USER_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("$.fullName").value(USER_1_FULL_NAME)
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void updateUserByAdminUserAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        UserResource resource = prepareUser();
        resource.setFullName("new-full-name");
        resource.setEmail("new-full-name@app.com");

        mockMvc.perform(put(API_USERS + PATH_ID, USER_ID_3)
                .content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath(UserResource.Fields.fullName).value(resource.getFullName()),
                        jsonPath(UserResource.Fields.email).value(resource.getEmail())
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteUserByOwnerUserAndGet403PermissionDenied() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(delete(API_USERS + PATH_ID, USER_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void listUsersAndCheckResultIsValid() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(get(API_USERS))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("content").isArray(),
                        jsonPath("totalElements").isNumber(),
                        jsonPath("totalPages").isNumber()
                ));
    }

    private UserResource prepareUser() {
        UserResource resource = new UserResource();
        resource.setEmail("talehsmail@gmail.com");
        resource.setFullName(USER_1_FULL_NAME);
        resource.setPassword("pass123321");
        resource.setRole(UserRole.REGULAR);
        return resource;
    }

}

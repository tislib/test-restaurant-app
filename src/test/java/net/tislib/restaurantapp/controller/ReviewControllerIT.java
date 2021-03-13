package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.ReviewResource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;

import static net.tislib.restaurantapp.constant.ApiConstants.API_REVIEWS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_OWNER_REPLY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewControllerIT extends BaseIntegrationTest {

    public static final int RESTAURANT_ID = 101;
    public static final int REVIEW_ID_1 = 101;
    public static final int REVIEW_ID_2 = 102;
    public static final int REVIEW_ID_3 = 103;

    @Test
    @SuppressWarnings("PMD")
    public void createReviewAndGet200Success() throws Exception {
        auth(TestUser.ADMIN_USER);

        ReviewResource resource = prepareReview();

        mockMvc.perform(post(API_REVIEWS, RESTAURANT_ID).content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ))
                .andExpect(jsonPath(ReviewResource.Fields.comment).value(resource.getComment()));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteReviewByAdminReviewAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(delete(API_REVIEWS + PATH_ID, RESTAURANT_ID, REVIEW_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ));

        mockMvc.perform(get(API_REVIEWS + PATH_ID, RESTAURANT_ID, REVIEW_ID_2))
                .andExpect(ResultMatcher.matchAll(
                        status().isNotFound()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void getReviewByAdminReviewAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        mockMvc.perform(get(API_REVIEWS + PATH_ID, RESTAURANT_ID, REVIEW_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("$.comment").value("test-comment-123")
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void updateReviewByAdminReviewAndGet200() throws Exception {
        auth(TestUser.ADMIN_USER);

        ReviewResource resource = prepareReview();
        resource.setComment("test123-test321");

        mockMvc.perform(put(API_REVIEWS + PATH_ID, RESTAURANT_ID, REVIEW_ID_3)
                .content(jsonContent(resource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath(ReviewResource.Fields.comment).value(resource.getComment())
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void deleteReviewByOwnerReviewAndGet403PermissionDenied() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(delete(API_REVIEWS + PATH_ID, RESTAURANT_ID, REVIEW_ID_1))
                .andExpect(ResultMatcher.matchAll(
                        status().isForbidden()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void listReviewsAndCheckResultIsValid() throws Exception {
        auth(TestUser.OWNER_USER_1);

        mockMvc.perform(get(API_REVIEWS, RESTAURANT_ID))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("content").isArray(),
                        jsonPath("totalElements").isNumber(),
                        jsonPath("totalPages").isNumber()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void ownerReplyAndGet200Ok() throws Exception {
        auth(TestUser.OWNER_USER_1);

        OwnerReplyResource ownerReplyResource = new OwnerReplyResource();
        ownerReplyResource.setComment("test-replied-comment");

        mockMvc.perform(put(API_REVIEWS + PATH_OWNER_REPLY, RESTAURANT_ID, REVIEW_ID_1)
                .content(jsonContent(ownerReplyResource)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk(),
                        jsonPath("comment").value(ownerReplyResource.getComment())
                ));
    }

    private ReviewResource prepareReview() {
        ReviewResource resource = new ReviewResource();
        resource.setComment("comment123-1");
        resource.setDateOfVisit(LocalDate.now());
        resource.setStarCount((short) 3);
        resource.setUser(currentUser);

        return resource;
    }

}

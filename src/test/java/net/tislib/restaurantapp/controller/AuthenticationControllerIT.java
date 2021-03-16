package net.tislib.restaurantapp.controller;

import net.tislib.restaurantapp.base.BaseIntegrationTest;
import net.tislib.restaurantapp.base.TestUser;
import net.tislib.restaurantapp.constant.SecurityConstants;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenRefreshRequest;
import net.tislib.restaurantapp.data.authentication.UserRegistrationRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.API_USERS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_REGISTER;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerIT extends BaseIntegrationTest {

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2MTU4ODE3NjcsImV4cCI6MTYxNTg4MTc5NywidXNlclJvbGUiOiJBRE1JTiIsInVzZXJFbWFpbCI6ImFkbWluQHRlc3RhcHAuY29tIiwidXNlcklkIjoxMDQsInRva2VuVHlwZSI6ImFjY2VzcyJ9.aiopar1PNRjmnk_bZpSpzIUVKnIVEaDmBL28DTN8nXjkseq4-EHjMjM0gkYTooyq0KvarbUDn_VXSXvWCgauHA";
    private static final String INVALID_TOKEN = "AN_INVALID_TOKEN";
    private static final String INVALID_TOKEN_STRUCTURE = "BASIC k93kd92k9e"; // basic instead of bearer
    private static final String INVALID_TOKEN_STRUCTURE2 = "Bearer k93kd 92k9e"; // additional space

    @Test
    @SuppressWarnings("PMD")
    public void refreshTokenWithRefreshTokenAndGetSuccess() throws Exception {
        // arrange
        auth(TestUser.ADMIN_USER);

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken(refreshToken);

        // Act & Assert
        byte[] newAccessTokenData = mockMvcMain.perform(patch(API_AUTHENTICATION + PATH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(tokenRefreshRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        TokenPair.TokenDetails accessTokenDetails = mapper.readValue(newAccessTokenData, TokenPair.TokenDetails.class);

        Assertions.assertTrue(accessTokenDetails != null && StringUtils.isNotBlank(accessTokenDetails.getContent()));

        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER,
                        prepareAuthorizationHeaderValue(accessTokenDetails.getContent())))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void refreshTokenWithAccessTokenAndFail() throws Exception {
        // arrange
        auth(TestUser.ADMIN_USER);

        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        tokenRefreshRequest.setRefreshToken(accessToken);

        // Act & Assert
        mockMvcMain.perform(patch(API_AUTHENTICATION + PATH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(tokenRefreshRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().isBadRequest()
                ));

    }

    @Test
    @SuppressWarnings("PMD")
    public void authenticationShouldBeIgnoredAndRequestTerminatedInCaseOfOptionsRequest() throws Exception {
        mockMvcMain.perform(options(API_USERS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void authenticateWithAccessTokenButFailWithRefreshToken() throws Exception {
        // arrange
        auth(TestUser.ADMIN_USER);

        // act 1 : get user list successfully with refresh token
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER,
                        prepareAuthorizationHeaderValue(accessToken)))
                .andExpect(ResultMatcher.matchAll(
                        status().isOk()
                ));

        // act 2 : fail to get user list with refresh token
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER,
                        prepareAuthorizationHeaderValue(refreshToken)))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void useExpiredTokenAndFail() throws Exception {
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER,
                        prepareAuthorizationHeaderValue(EXPIRED_TOKEN)))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void useInvalidTokenAndFail() throws Exception {
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER,
                        prepareAuthorizationHeaderValue(INVALID_TOKEN)))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void useInvalidTokenStructureAndFail() throws Exception {
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER, INVALID_TOKEN_STRUCTURE))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void useInvalidTokenStructureWithAdditionalSpaceAndFail() throws Exception {
        mockMvcMain.perform(get(API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.AUTHORIZATION_HEADER, INVALID_TOKEN_STRUCTURE2))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void registerAndGetSuccess() throws Exception {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();

        userRegistrationRequest.setEmail("test-register-email1@app.com");
        userRegistrationRequest.setPassword("test-password-123");
        userRegistrationRequest.setFullName("test user1");

        mockMvcMain.perform(post(API_AUTHENTICATION + PATH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(userRegistrationRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().is2xxSuccessful()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void authenticationWithWrongCredentialsAndGet401Unauthorized() throws Exception {
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();

        tokenCreateRequest.setEmail("wrong-email@wrong-email.com");
        tokenCreateRequest.setPassword("wrong password1");

        mockMvcMain.perform(post(API_AUTHENTICATION + PATH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(tokenCreateRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void authenticationWithWrongPasswordAndGet401Unauthorized() throws Exception {
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();

        tokenCreateRequest.setEmail("admin@app.com");
        tokenCreateRequest.setPassword("wrong password2");

        mockMvcMain.perform(post(API_AUTHENTICATION + PATH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(tokenCreateRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().isUnauthorized()
                ));
    }

    @Test
    @SuppressWarnings("PMD")
    public void registerWithExistingMailAndGetError() throws Exception {
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();

        userRegistrationRequest.setEmail("test-register-email2@app.com");
        userRegistrationRequest.setPassword("test-password-321");
        userRegistrationRequest.setFullName("test user2");

        mockMvcMain.perform(post(API_AUTHENTICATION + PATH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(userRegistrationRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().is2xxSuccessful()
                ));

        mockMvcMain.perform(post(API_AUTHENTICATION + PATH_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(userRegistrationRequest)))
                .andExpect(ResultMatcher.matchAll(
                        status().isBadRequest()
                ));
    }

}

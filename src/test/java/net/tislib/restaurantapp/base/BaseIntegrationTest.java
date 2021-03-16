package net.tislib.restaurantapp.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.tislib.restaurantapp.constant.SecurityConstants;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;
import static net.tislib.restaurantapp.constant.SecurityConstants.BEARER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles({"integration-test", "h2"})
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    protected final MockMvcExtended mockMvc = new MockMvcExtended();

    @Autowired
    protected MockMvc mockMvcMain;

    @Autowired
    protected ObjectMapper mapper;

    protected String accessToken;

    protected UserResource currentUser;
    protected String refreshToken;

    public class MockMvcExtended {
        @SneakyThrows
        public ResultActions perform(RequestBuilder requestBuilder) {
            return mockMvcMain.perform(servletContext -> {
                MockHttpServletRequest request = requestBuilder.buildRequest(servletContext);
                request.addHeader(SecurityConstants.AUTHORIZATION_HEADER, prepareAuthorizationHeaderValue(accessToken));
                request.setContentType(MediaType.APPLICATION_JSON.toString());
                return request;
            });
        }
    }

    @SneakyThrows
    public void auth(TestUser testUser) {
        tokenAuth(testUser);
        readCurrentUser();
    }

    @SneakyThrows
    private void readCurrentUser() {
        String content = mockMvc.perform(get(API_AUTHENTICATION + PATH_TOKEN))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenUserDetails result = mapper.readValue(content, TokenUserDetails.class);

        this.currentUser = result.getUser();
    }

    @SneakyThrows
    private void tokenAuth(TestUser testUser) {
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();
        tokenCreateRequest.setEmail(testUser.getEmail());
        tokenCreateRequest.setPassword(testUser.getPassword());

        String content = mockMvcMain.perform(post(API_AUTHENTICATION + PATH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent(tokenCreateRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenPair result = mapper.readValue(content, TokenPair.class);

        this.accessToken = result.getAccessToken().getContent();
        this.refreshToken = result.getRefreshToken().getContent();
    }

    @SneakyThrows
    public byte[] jsonContent(Object object) {
        return mapper.writeValueAsBytes(object);
    }



    public static String prepareAuthorizationHeaderValue(String token) {
        return BEARER + " " + token;
    }

}


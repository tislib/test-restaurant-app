package net.tislib.restaurantapp.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.tislib.restaurantapp.data.UserResource;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import net.tislib.restaurantapp.data.authentication.TokenPair;
import net.tislib.restaurantapp.data.authentication.TokenUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;
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

    public class MockMvcExtended {
        public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
            return mockMvcMain.perform(servletContext -> {
                MockHttpServletRequest res = requestBuilder.buildRequest(servletContext);
                res.addHeader("Authorization", "Bearer " + accessToken);
                res.setContentType("application/json");
                return res;
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

    private void tokenAuth(TestUser testUser) throws Exception {
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();
        tokenCreateRequest.setEmail(testUser.getEmail());
        tokenCreateRequest.setPassword(testUser.getPassword());

        String content = mockMvcMain.perform(post(API_AUTHENTICATION + PATH_TOKEN)
                .contentType("application/json")
                .content(jsonContent(tokenCreateRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenPair result = mapper.readValue(content, TokenPair.class);

        this.accessToken = result.getAccessToken().getContent();
    }

    @SneakyThrows
    public byte[] jsonContent(Object object) {
        return mapper.writeValueAsBytes(object);
    }

}


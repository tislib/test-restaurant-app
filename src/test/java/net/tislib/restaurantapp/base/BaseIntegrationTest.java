package net.tislib.restaurantapp.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.tislib.restaurantapp.data.authentication.TokenCreateRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_TOKEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles({"integration-test", "h2"})
@ExtendWith(SpringExtension.class)
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @SneakyThrows
    public void auth(TestUser testUser) {
        TokenCreateRequest tokenCreateRequest = new TokenCreateRequest();
        tokenCreateRequest.setEmail(testUser.getEmail());
        tokenCreateRequest.setPassword(testUser.getPassword());

        Object result = mockMvc.perform(post(API_AUTHENTICATION + PATH_TOKEN).content(jsonContent(tokenCreateRequest)))
                .andReturn()
                .getAsyncResult();
        System.out.println(result);
    }

    @SneakyThrows
    public byte[] jsonContent(Object object) {
        return mapper.writeValueAsBytes(object);
    }

}


package net.tislib.restaurantapp.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.data.authentication.ErrorResponse;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.tislib.restaurantapp.constant.SecurityConstants.AUTHORIZATION_HEADER_STRING;

@Log4j2
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_STRING);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }

        if (StringUtils.isBlank(authorizationHeader)) {
            chain.doFilter(request, response);
            return;
        }

        String[] authorizationHeaderParts = authorizationHeader.split(" ");

        if (authorizationHeaderParts.length != 2) {
            sendError(response, "invalid token parts");
            return;
        }

        if (!authorizationHeaderParts[0].equalsIgnoreCase("bearer")) {
            sendError(response, "bearer token expected");
            return;
        }

        String token = authorizationHeaderParts[1];

        TokenAuthentication tokenAuthentication = authenticationService.getTokenAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);

        chain.doFilter(request, response);
    }

    @SneakyThrows
    private void sendError(HttpServletResponse response, String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setException("AuthenticationException");
        errorResponse.setMessage(message);
        log.warn(message);

        response.setStatus(403);
        response.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
        response.getOutputStream().close();
    }
}

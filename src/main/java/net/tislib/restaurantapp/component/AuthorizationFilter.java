package net.tislib.restaurantapp.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.tislib.restaurantapp.data.authentication.ErrorResponse;
import net.tislib.restaurantapp.data.authentication.TokenAuthentication;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static net.tislib.restaurantapp.constant.SecurityConstants.AUTHORIZATION_HEADER;
import static net.tislib.restaurantapp.constant.SecurityConstants.BEARER;

@Log4j2
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private static final int AUTHORIZATION_HEADER_PART_COUNT = 2; // 1: token-type(Bearer); 2: token itself
    private static final String SPACE = " ";
    private static final String USER_ID = "userId";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        // apply cors headers to all requests, it is specially needed when options request is sent by browser
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");

        // if we accept options request from browser then finish request
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            response.setStatus(200);
            log.debug("option request accepted for " + logRequest(request));
            return;
        }

        // read authorization header
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        /* check if authorization token is empty, if empty then we will not handle authentication logic
           spring security will handle what to do with unauthenticated requests
         */
        if (StringUtils.isBlank(authorizationHeader)) {
            log.debug("no authorization field for " + logRequest(request));
            chain.doFilter(request, response);
            return;
        }

        // split header to to parts, token type(bearer) and token itself
        String[] authorizationHeaderParts = authorizationHeader.split(SPACE);

        // if header parts count is different, it means that header structure is wrong
        if (authorizationHeaderParts.length != AUTHORIZATION_HEADER_PART_COUNT) {
            sendError(request, response, "invalid token parts");
            return;
        }

        // if token type is not bearer token (e.g. basic, etc.) it means that we need to fail request
        if (!authorizationHeaderParts[0].equalsIgnoreCase(BEARER)) {
            sendError(request, response, "bearer token expected");
            return;
        }

        String token = authorizationHeaderParts[1];

        // authentication object is prepared from the token we prepared
        Optional<TokenAuthentication> tokenAuthenticationOptional = authenticationService.getTokenAuthentication(token);

        if (tokenAuthenticationOptional.isEmpty()) {
            sendError(request, response, "token is invalid");
            return;
        }

        TokenAuthentication tokenAuthentication = tokenAuthenticationOptional.get();

        log.debug("user authenticated: {} request: {}",
                tokenAuthentication.getName(),
                logRequest(request));

        // pass authentication token to spring security
        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);

        try {
            /* MDC is used for logging purposes, userId MDC variable is passed to logback
               check logback.xml:2
            */

            MDC.put(USER_ID, String.valueOf(tokenAuthentication.getUserId()));

            chain.doFilter(request, response);
        } finally {
            // clear MDC userId variable to prevent userId leakage cross threads
            MDC.remove(USER_ID);
        }
    }

    private String logRequest(HttpServletRequest request) {
        return " URI: " + request.getRequestURI() +
                " Method: " + request.getMethod();
    }

    // we are sending request to error if the token is invalid
    @SneakyThrows
    private void sendError(HttpServletRequest request, HttpServletResponse response, String message) {
        ErrorResponse errorResponse = new ErrorResponse(message);
        log.warn(message + logRequest(request));

        response.setStatus(401);
        response.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
        response.getOutputStream().close();
    }
}

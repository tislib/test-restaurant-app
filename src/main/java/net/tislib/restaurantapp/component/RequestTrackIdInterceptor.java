package net.tislib.restaurantapp.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class RequestTrackIdInterceptor extends OncePerRequestFilter {

    public static final char[] REQUEST_TRACK_ID_SYMBOLS = {
            '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static final String REQUEST_TRACK_ID = "requestTrackId";

    private final ThreadLocal<String> requestTrackIdThreadLocal = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestTrackId = request.getHeader(REQUEST_TRACK_ID);

            if (StringUtils.isBlank(requestTrackId)) {
                requestTrackId = initRequestTrackId();
            }

            MDC.put(REQUEST_TRACK_ID, requestTrackId);

            requestTrackIdThreadLocal.set(requestTrackId);

            filterChain.doFilter(request, response);
        } finally {
            requestTrackIdThreadLocal.remove();
            MDC.remove(REQUEST_TRACK_ID);
        }
    }

    private String initRequestTrackId() {
        return RandomStringUtils.random(16, REQUEST_TRACK_ID_SYMBOLS);
    }
}

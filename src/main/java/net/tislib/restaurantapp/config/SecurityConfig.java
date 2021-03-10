package net.tislib.restaurantapp.config;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.component.AuthorizationFilter;
import net.tislib.restaurantapp.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

import static net.tislib.restaurantapp.constant.ApiConstants.API_AUTHENTICATION;
import static net.tislib.restaurantapp.constant.ApiConstants.API_SWAGGER_API_DOCS;
import static net.tislib.restaurantapp.constant.ApiConstants.API_SWAGGER_UI;
import static net.tislib.restaurantapp.constant.ApiConstants.API_h2_CONSOLE;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ANT_MATCH_ALL;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().disable().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Our public endpoints
                // allow authentication endpoints to be called publicly
                .antMatchers(API_AUTHENTICATION + PATH_ANT_MATCH_ALL).permitAll()
                // swagger documentation urls are allowed publicly
                .antMatchers(API_SWAGGER_UI,
                        API_SWAGGER_UI + PATH_ANT_MATCH_ALL,
                        API_h2_CONSOLE + PATH_ANT_MATCH_ALL,
                        API_h2_CONSOLE,
                        API_SWAGGER_API_DOCS,
                        API_SWAGGER_API_DOCS + PATH_ANT_MATCH_ALL,
                        API_SWAGGER_UI + PATH_ANT_MATCH_ALL).permitAll()
                // require authentication by default
                .anyRequest().authenticated();

        http.addFilterBefore(new AuthorizationFilter(authenticationService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

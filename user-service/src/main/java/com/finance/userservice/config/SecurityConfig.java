package com.finance.userservice.config;

import com.finance.userservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpStatus.UNAUTHORIZED.value()
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write("""
                            {
                              "status": 401,
                              "error": "Unauthorized",
                              "message": "Authentication is required"
                            }
                            """);
                                }
                        )

                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpStatus.FORBIDDEN.value()
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write("""
                            {
                              "status": 403,
                              "error": "Forbidden",
                              "message": "You do not have permission to access this resource"
                            }
                            """);
                                }
                        )
                )

                .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/api/auth/**"
                ).permitAll()

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/users"
                ).permitAll()

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/users/**"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/users/**"
                ).hasAnyRole("USER", "ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/users/**"
                ).hasRole("ADMIN")

                .anyRequest().authenticated()
        )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
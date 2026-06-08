package com.onlineassessment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {

        httpSecurity
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/users/register",
                                "/users/login"
                        ).permitAll()

                        // Session APIs — authenticated users only
                        .requestMatchers(
                                HttpMethod.POST,
                                "/sessions/start/**"
                        ).hasAnyRole("ADMIN", "USER")

                        //Overview endpoint
                        .requestMatchers(HttpMethod.GET, "/overview/**")
                        .hasAnyRole("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/sessions/**"
                        ).hasAnyRole("ADMIN", "USER")

                        // Question APIs
                        .requestMatchers(
                                HttpMethod.GET,
                                "/questions/**"
                        ).hasAnyRole("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/questions/**"
                        ).hasRole("ADMIN")

                        // Test APIs (existing admin flow — untouched)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/tests/**"
                        ).hasAnyRole("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/tests/*/start"
                        ).hasAnyRole("ADMIN", "USER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/tests"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }
}
package com.zuno.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints — no auth required
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/api/listings/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // Everything else requires authentication (will be enforced in Sprint 4)
                .anyRequest().permitAll() // TODO: Change to .authenticated() in T-22
            );

        return http.build();
    }
}

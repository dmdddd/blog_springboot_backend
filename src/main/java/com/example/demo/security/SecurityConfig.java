package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final FirebaseTokenFilter firebaseTokenFilter;

    public SecurityConfig(FirebaseTokenFilter firebaseTokenFilter) {
        this.firebaseTokenFilter = firebaseTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            // Articles
                            .requestMatchers(HttpMethod.POST, "/api/articles").authenticated() // Require authentication for POST
                            .requestMatchers("/api/articles/*/vote").authenticated()        // Require authentication
                            .requestMatchers("/api/articles/checkName/*").authenticated()   // Require authentication
                            .requestMatchers("/api/articles/*").permitAll()                 // Open access
                            // Comments
                            .requestMatchers(HttpMethod.POST, "/api/articles/*/comments").authenticated() // Require authentication for POST
                            .requestMatchers("/api/comments/*").authenticated()             // Require authentication
                            .requestMatchers("/api/comments/updateIcon").authenticated()    // Require authentication
                            // Fallback for any other requests
                            .anyRequest().permitAll())
                            
                    .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
    }
}
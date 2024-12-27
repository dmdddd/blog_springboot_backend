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
                // Blogs
                .requestMatchers(HttpMethod.POST, "/api/blogs").authenticated()
                .requestMatchers("/api/blogs/checkSlug/*").authenticated()

                // Articles
                .requestMatchers(HttpMethod.POST, "/api/blogs/*/articles").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/blogs/*/articles/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/*/articles/*").authenticated()
                .requestMatchers("/api/blogs/*/articles/checkSlug/*").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/blogs/*/articles/*/vote").authenticated()

                // Pages
                .requestMatchers(HttpMethod.PUT, "/api/blogs/*/pages/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/blogs/*/pages/*").authenticated()

                // Comments
                .requestMatchers(HttpMethod.POST, "/api/blogs/*/articles/*/comments").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/comments/*").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/comments/*").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/comments/updateIcon").authenticated()

                // Fallback for any other requests
                .anyRequest().permitAll())
                            
            .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
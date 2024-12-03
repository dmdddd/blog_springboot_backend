package com.example.demo.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FirebaseTokenFilter extends org.springframework.web.filter.OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("authtoken");
        if (header != null) {
            String token = request.getHeader("authtoken");
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                // // Log user information for debugging
                System.out.println("Authenticated User ID: " + decodedToken.getUid());
                System.out.println("Authenticated User Email: " + decodedToken.getEmail());
                System.out.println("Authenticated User Name: " + decodedToken.getName());

                // Create an authentication object for Spring Security
                PreAuthenticatedAuthenticationToken authentication =
                        new PreAuthenticatedAuthenticationToken(
                                decodedToken.getUid(), // Use UID as principal
                                null, // No credentials required
                                null  // Authorities, if needed
                        );

                // Store the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (FirebaseAuthException e) {
                System.out.println("Token: " + token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
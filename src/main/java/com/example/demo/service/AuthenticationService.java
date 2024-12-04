package com.example.demo.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.security.FirebaseUserDetails;

@Service
public class AuthenticationService {

    private static final FirebaseUserDetails DEFAULT_USER = new FirebaseUserDetails("", "", "");


    public FirebaseUserDetails getCurrentUserDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getDetails() instanceof FirebaseUserDetails) {
            return (FirebaseUserDetails) authentication.getDetails();
        }

        return DEFAULT_USER;
    }

    public String getCurrentUserEmail() {
        FirebaseUserDetails userDetails = getCurrentUserDetails();
        return userDetails.getEmail();
    }

    public String getCurrentUserPhotoUrl() {
        FirebaseUserDetails userDetails = getCurrentUserDetails();
        return userDetails.getPhotoUrl();
    }

    public String getCurrentUserName() {
        FirebaseUserDetails userDetails = getCurrentUserDetails();
        return userDetails.getName();
    }
}

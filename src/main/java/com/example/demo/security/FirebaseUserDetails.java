package com.example.demo.security;

/**
 * Represents details of a user authenticated via Firebase.
 */
public class FirebaseUserDetails {
    private final String email;
    private final String name;
    private final String photoUrl;

    public FirebaseUserDetails(String email, String name, String photoUrl) {
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String toString() {
        return "FirebaseUserDetails{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}

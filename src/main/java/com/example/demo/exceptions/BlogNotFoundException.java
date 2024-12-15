package com.example.demo.exceptions;

public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}

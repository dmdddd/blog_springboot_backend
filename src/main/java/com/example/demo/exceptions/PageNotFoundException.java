package com.example.demo.exceptions;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}
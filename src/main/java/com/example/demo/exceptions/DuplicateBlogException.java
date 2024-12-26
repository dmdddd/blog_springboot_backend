package com.example.demo.exceptions;

public class DuplicateBlogException extends RuntimeException {
    public DuplicateBlogException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}
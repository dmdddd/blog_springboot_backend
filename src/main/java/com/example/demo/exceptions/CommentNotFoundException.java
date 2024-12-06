package com.example.demo.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}
package com.example.demo.exceptions;

public class DuplicateArticleException extends RuntimeException {
    public DuplicateArticleException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}

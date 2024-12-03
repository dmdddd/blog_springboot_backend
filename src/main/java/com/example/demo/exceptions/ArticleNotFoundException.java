package com.example.demo.exceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}

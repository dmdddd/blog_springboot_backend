package com.example.demo.exceptions;

public class InvalidSlugFormatException extends RuntimeException {
    public InvalidSlugFormatException(String message) {
        super(message);  // Pass the custom error message to the parent class
    }
}
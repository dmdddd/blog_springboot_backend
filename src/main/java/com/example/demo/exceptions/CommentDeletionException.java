package com.example.demo.exceptions;

public class CommentDeletionException extends RuntimeException {

    public CommentDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.example.demo.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Maps these exceptions to meaningful HTTP responses with appropriate status codes (e.g., 404 Not Found, 400 Bad Request).
// Authentication exception is being thrown by the Firebase module

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBlogNotFoundException(BlogNotFoundException ex) {
        logger.warn("Blog not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleArticleNotFoundException(ArticleNotFoundException ex) {
        logger.warn("Article not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SlugAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSlugAlreadyExistsException(SlugAlreadyExistsException ex) {
        logger.warn("Slug already exists: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePageNotFoundException(PageNotFoundException ex) {
        logger.warn("Page not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        logger.warn("Invalid reqest: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        logger.warn("Illegal state: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidSlugFormatException.class)
    public ResponseEntity<ErrorResponse> InvalidSlugFormatException(InvalidSlugFormatException ex) {
        logger.warn("Illegal slug: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("BAD_REQUEST", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateArticleException.class)
    public ResponseEntity<ErrorResponse> DuplicateArticleException(DuplicateArticleException ex) {
        logger.warn("Article with this slug already exists: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("CONFLICT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex); // Log stack trace
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            validationErrorResponse.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(validationErrorResponse);
    }
}

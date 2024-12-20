package com.example.demo.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationErrorResponse {
private Map<String, List<String>> errors = new HashMap<>();

    public void addError(String field, String message) {
        this.errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}

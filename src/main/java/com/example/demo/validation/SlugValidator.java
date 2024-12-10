package com.example.demo.validation;

import java.util.regex.Pattern;

public class SlugValidator {
    
    private static final String SLUG_REGEX = "^[a-z0-9]+(?:-[a-z0-9]+)*$";
    private static final Pattern SLUG_PATTERN = Pattern.compile(SLUG_REGEX);

    public static boolean isValidSlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            return false;
        }
        return SLUG_PATTERN.matcher(slug).matches();
    }
}

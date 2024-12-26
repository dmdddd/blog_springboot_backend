package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogRequestDto {
    @NotBlank(message = "Blog name is required")
    private String name; // Slug

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Pattern(regexp = "^[\\w\\s.,!?-]+$", message = "Title can only contain letters, numbers, and basic punctuation")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Pattern(regexp = "^[\\w\\s.,!?-]+$", message = "Title can only contain letters, numbers, and basic punctuation")
    private String description;

}

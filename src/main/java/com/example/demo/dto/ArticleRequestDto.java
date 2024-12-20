package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleRequestDto {
    @NotBlank(message = "Blog is required")
    private String blog;

    @NotBlank(message = "Slug is required")
    private String name; // Slug

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Pattern(regexp = "^[\\w\\s.,!?-]+$", message = "Title can only contain letters, numbers, and basic punctuation")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 50000, message = "Content must be between 10 and 50,000 characters")
    private String content;
}
package com.example.demo.dto;

import lombok.Data;

@Data
public class ArticleRequestDto {
    private String blog;
    private String name; // Slug
    private String title;
    private String text;
}
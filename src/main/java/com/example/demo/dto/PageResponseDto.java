package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class PageResponseDto {
    private final String _id;
    private final String blog;
    private final String slug;
    private final String title;
    private final String content;
    private final int order;
    private final Date createdAt;
    private final Date updatedAt;
}
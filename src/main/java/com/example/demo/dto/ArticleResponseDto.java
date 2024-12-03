package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class ArticleResponseDto {
    private String id;
    private String name;
    private String title;
    private List<String> content;
    private int upvotes = 0;
    private List<String> upvoteIds = new ArrayList<>();
    // DTO only fields
    private final boolean canUpvote;
}

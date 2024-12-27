package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class ArticleResponseDto {
    private String id;
    private String name;
    private String blog;
    private String title;
    private String content;
    private int upvotes = 0;
    private List<String> upvoteIds = new ArrayList<>();
    private final boolean canUpvote; // DTO only field
    private String author;
    private Date createdAt;
    private Date updatedAt;
    private boolean isAdmin;
    private boolean isEditor;
    
}

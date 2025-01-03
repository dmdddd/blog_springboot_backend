package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class BlogResponseDto {
    private String id;
    private String name;
    private String title;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private boolean isAdmin;
    private boolean isEditor;
    private int articleCount = 0;
}

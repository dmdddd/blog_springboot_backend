package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class CommentResponseDto {
    private final String _id;
    private final String postedBy;
    private final String text;
    private final String blog;
    private final String articleName;
    private final String userEmail;
    private final String userIcon;
    private final Date createdAt;
    private final Date updatedAt;
    // DTO only fields
    private final boolean canDelete;
}

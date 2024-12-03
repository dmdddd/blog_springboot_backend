package com.example.demo.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class CommentResponseDto {
    private final String id;
    private final String postedBy;
    private final String text;
    private final String articleName;
    private final String userEmail;
    private final String userIcon;
    private final Date createdOn;
    // DTO only fields
    private final boolean canDelete;
}

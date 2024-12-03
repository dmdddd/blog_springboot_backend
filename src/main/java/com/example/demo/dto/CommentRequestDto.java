package com.example.demo.dto;

import lombok.Data;

@Data
// @AllArgsConstructor // Generates a constructor with all fields
public class CommentRequestDto {
    private String postedBy;
    private String text;
    private String articleName;
    private String userEmail;
    private String userIcon;
}

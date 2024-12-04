package com.example.demo.dto;

import lombok.Data;

@Data
// @AllArgsConstructor // Generates a constructor with all fields
public class CommentRequestDto {
    private String text;
}

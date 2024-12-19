package com.example.demo.dto;
import lombok.Data;

@Data
public class PageRequestDto {
    private String slug;
    private String title;
    private String content;
}
package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
public class BlogResponseDto {
    private String id;
    private String name;
    private String title;
    private String description;
    private Date createdOn;
    private List<String> admins = new ArrayList<>();

}

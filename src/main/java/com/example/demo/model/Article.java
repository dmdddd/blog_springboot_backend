package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@Document(collection = "articles")
public class Article {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("title")
    private String title;
    @Field("content")
    private List<String> content;
    @Field("upvotes")
    private int upvotes;
    @Field("upvoteIds")
    private List<String> upvoteIds;
}

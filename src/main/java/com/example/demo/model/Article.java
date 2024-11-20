package com.example.demo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "articles")
public class Article {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("upvotes")
    private int upvotes;
    @Field("upvoteIds")
    private List<String> upvoteIds;
}

package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor  // Needed to use default for fields
@Document(collection = "articles")
public class Article {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("blog")
    private String blog;
    @Field("title")
    private String title;
    @Field("content")
    private List<String> content;
    @Field("upvotes")
    private int upvotes = 0;
    @Field("upvoteIds")
    private List<String> upvoteIds = new ArrayList<>();


    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", blog='" + blog + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", content=" + content +
                '}';
    }
}

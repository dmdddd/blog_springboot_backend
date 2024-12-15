package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
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
@Document(collection = "pages")
public class Page {
    @Id
    private String id;
    @Field("blog")
    private String blog;
    @Field("slug")
    private String slug;
    @Field("title")
    private String title;
    @Field("content")
    private String content;
    @Field("order")
    private int order;
    @Field("createdAt")
    private Date createdAt;
    @Field("updatedAt")
    private Date updatedAt;


    @Override
    public String toString() {
        return "Page{" +
                "id='" + id + '\'' +
                ", blog='" + blog + '\'' +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", order='" + order + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

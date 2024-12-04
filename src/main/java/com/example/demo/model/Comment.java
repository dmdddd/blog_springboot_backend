package com.example.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    @Field("postedBy")
    private String postedBy;
    @Field("text")
    private String text;
    @Field("articleName")
    private String articleName;
    @Field("userEmail")
    private String userEmail;
    @Field("userIcon")
    private String userIcon;
    @Field("createdOn")
    private Date createdOn;

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", article='" + articleName + '\'' +
                ", text='" + text + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
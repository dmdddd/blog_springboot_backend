package com.example.demo.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@Document(collection = "comments")
@CompoundIndexes({
    @CompoundIndex(name = "blog_article_idx", def = "{'blog': 1, 'articleName': 1}")
})
public class Comment {

    @Id
    private String id;

    @Field("postedBy")
    private String postedBy;

    @Field("text")
    private String text;

    @Field("blog")
    private String blog;

    @Field("articleName")
    private String articleName;

    @Field("userEmail")
    private String userEmail;

    @Field("userIcon")
    private String userIcon;

    @Field("createdAt")
    private Date createdAt = new Date();

    @Field("updatedAt")
    private Date updatedAt = new Date();

    @Override
    public String toString() {
        String maskedEmail = (userEmail != null) ? userEmail.replaceAll("(?<=.{2}).(?=.*@)", "*") : null;

        return "Comment{" +
                "id='" + id + '\'' +
                ", article='" + articleName + '\'' +
                ", text='" + text + '\'' +
                ", userEmail='" + maskedEmail + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor  // Needed to use default for fields
@Document(collection = "articles")
@CompoundIndexes({
    @CompoundIndex(name = "blog_article_idx", def = "{'blog': 1, 'name': 1}", unique = true)
})
public class Article {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Indexed
    @Field("blog")
    private String blog;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("upvotes")
    private int upvotes = 0;

    @Field("upvoteIds")
    private List<String> upvoteIds = new ArrayList<>();

    @Field("author")
    private String author;

    @Field("createdAt")
    private Date createdAt = new Date();

    @Indexed
    @Field("updatedAt")
    private Date updatedAt = new Date();

    @Field("admins")
    private List<String> blogAdminIds = new ArrayList<>();

    @Field("editors")
    private List<String> blogEditorIds = new ArrayList<>();


    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", blog='" + blog + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + (author != null ? "****" : null) + '\'' +  // Masking author
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

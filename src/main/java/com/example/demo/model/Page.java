package com.example.demo.model;

import java.util.Date;

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
@Document(collection = "pages")
@CompoundIndexes({
    @CompoundIndex(name = "blog_slug_idx", def = "{'blog': 1, 'slug': 1}", unique = true)
})
public class Page {

    @Id
    private String id;

    @Field("blog")
    @Indexed
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

    public String toString() {
    return "Page{" +
            "id='" + id + '\'' +
            ", blog='" + blog + '\'' +
            ", slug='" + slug + '\'' +
            ", title='" + title + '\'' +
            ", content='" + (content != null && content.length() > 50 ? content.substring(0, 50) + "..." : content) + '\'' +  // Truncated content
            ", order=" + order +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
}
}

package com.example.demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor  // Needed to use default for fields
@Document(collection = "blogs")
public class Blog {

    @Id
    private String id;

    @Indexed
    @Field("name")
    private String name;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("createdAt")
    private Date createdAt = new Date();

    @Field("updatedAt")
    private Date updatedAt = new Date();

    @Field("users")
    private List<BlogUser> users = new ArrayList<>();

    @Override
    public String toString() {
        // Truncate description to 50 characters to avoid long output
        String truncatedDescription = description != null && description.length() > 50 
                                      ? description.substring(0, 50) + "..." 
                                      : description;
        
        return "Blog{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + truncatedDescription + '\'' +  // Truncated description
                '}';
    }
}

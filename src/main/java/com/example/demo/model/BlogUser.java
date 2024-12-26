package com.example.demo.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor // Generates a constructor with all fields
@Data
public class BlogUser {

    public enum BlogRole {
        ADMIN,
        EDITOR
    }

    private String uid;      // Firebase UID
    private String email;
    private String name;
    private BlogRole role;
    private Date addedAt;

}

package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class BlogUser {

    public enum Role {
        ADMIN,
        EDITOR
    }

    private String uid;      // Firebase UID
    private String email;
    private String name;
    private Role role;
    private Date addedAt;

}

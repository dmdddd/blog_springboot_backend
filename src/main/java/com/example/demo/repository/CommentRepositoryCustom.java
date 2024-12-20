package com.example.demo.repository;

public interface CommentRepositoryCustom {
    long updatePhotoUrlForEmail(String email, String photoUrl);
    long updateCommentsArticleName(String blog, String oldName, String newName);
}

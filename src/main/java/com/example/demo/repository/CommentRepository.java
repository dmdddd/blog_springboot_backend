package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {
    List<Comment> findByBlogAndArticleName(String blog, String article);
    void deleteByBlogAndArticleName(String blog, String article);
}


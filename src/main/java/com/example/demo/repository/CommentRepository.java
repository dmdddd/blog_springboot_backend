package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByArticleName(String name);
}


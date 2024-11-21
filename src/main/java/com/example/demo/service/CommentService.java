package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // Constructor injection
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllCommentsOfArticle(String articleName) {
        return commentRepository.findByArticleName(articleName);
    }

}

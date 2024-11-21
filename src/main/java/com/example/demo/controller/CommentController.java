package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;

@RestController
@RequestMapping("/api/comments/")
public class CommentController {
    @Autowired
    private CommentService commentService;
    

    // GET endpoint to retrieve all comments on an article
    @GetMapping("/{article_name}")
    public ResponseEntity<List<Comment>> getAllCommentsOfArticle(@PathVariable("article_name") String articleName) {
        List<Comment> comments = commentService.getAllCommentsOfArticle(articleName);

        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build(); // No content (204) if empty
        }
        return ResponseEntity.ok(comments); // 200 OK with the articles
    }

}

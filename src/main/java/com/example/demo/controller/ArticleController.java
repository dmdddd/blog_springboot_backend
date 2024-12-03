package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.exceptions.ArticleNotFoundException;
import com.example.demo.model.Article;
import com.example.demo.service.ArticleService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // GET endpoint to retrieve all articles
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        
        List<ArticleResponseDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles); // 200 OK with the articles
    }

    @GetMapping("/{article_name}")
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable("article_name") String name) {

        ArticleResponseDto articleResponseDto = articleService.getArticleByName(name);
        return ResponseEntity.ok(articleResponseDto); // Return ArticleDTO with 200 OK
    }

    @PutMapping("/{article_name}/upvote")
    public ResponseEntity<ArticleResponseDto> upvoteArticle(@PathVariable("article_name") String articleName) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ArticleResponseDto updatedArticle = articleService.upvoteArticle(articleName, userId);
        return ResponseEntity.ok(updatedArticle); // Return the updated article DTO with 200 OK
    }

    @PutMapping("/{article_name}/downvote")
    public ResponseEntity<ArticleResponseDto> downvoteArticle(@PathVariable("article_name") String articleName) {

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ArticleResponseDto downdatedArticle = articleService.downvoteArticle(articleName, userId);
        return ResponseEntity.ok(downdatedArticle); // Return the updated article DTO with 200 OK
    }

}

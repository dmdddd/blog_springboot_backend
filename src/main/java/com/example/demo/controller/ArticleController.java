package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.ArticleRequestDto;
import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.ArticleService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @GetMapping("/checkSlug/{slug}")
    public ResponseEntity<Map<String, Boolean>> checkSlug(@PathVariable String slug) {
        boolean isUnique = articleService.existsBySlug(slug);
        return ResponseEntity.ok(Map.of("isUnique", isUnique));
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleRequestDto articleRequest) {
        logger.info("Received request to create a new article: {}", articleRequest.getTitle());

        ArticleResponseDto newArticle = articleService.createArticle(articleRequest);

        logger.info("Successfully added a new article with ID: {}", newArticle.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newArticle.getId())
            .toUri();

        return ResponseEntity.created(location).body(newArticle);
    }
}

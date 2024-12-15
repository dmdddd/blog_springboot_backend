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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.ArticleRequestDto;
import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/blogs/{blog_name}/articles")
public class ArticleController {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

    // GET endpoint to retrieve all articles
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticlesByBlog(@PathVariable("blog_name") String blog) {

        List<ArticleResponseDto> articles = articleService.getArticlesByBlog(blog);
        return ResponseEntity.ok(articles); // 200 OK with the articles
    }

    @GetMapping("/{article_name}")
    public ResponseEntity<ArticleResponseDto> getArticleByBlogByName(@PathVariable("blog_name") String blog, @PathVariable("article_name") String name) {

        ArticleResponseDto articleResponseDto = articleService.findByBlogAndName(blog, name);
        return ResponseEntity.ok(articleResponseDto); // Return ArticleDTO with 200 OK
    }

    @PutMapping("/{article_name}/vote")
    public ResponseEntity<ArticleResponseDto> voteOnArticle(@PathVariable("blog_name") String blog, @PathVariable("article_name") String article, @RequestParam("type") String voteType) {
        logger.info("Request vote on article: {} for blog: {}, type: {} received", blog, article, voteType);
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ArticleResponseDto downdatedArticle = articleService.voteOnArticle(blog, article, voteType, userId);
        return ResponseEntity.ok(downdatedArticle); // Return the updated article DTO with 200 OK
    }

    @GetMapping("/checkSlug/{slug}")
    public ResponseEntity<Map<String, Boolean>> checkSlug(@PathVariable("blog_name") String blog, @PathVariable String slug) {
        boolean isUnique = articleService.existsByBlogAndSlug(blog, slug);
        return ResponseEntity.ok(Map.of("isUnique", isUnique));
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleRequestDto articleRequest) {
        logger.info("Received request to s new article: {}", articleRequest.getTitle());

        ArticleResponseDto newArticle = articleService.createArticle(articleRequest);

        logger.info("Successfully added a new article with ID: {}", newArticle.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newArticle.getId())
            .toUri();

        return ResponseEntity.created(location).body(newArticle);
    }
}

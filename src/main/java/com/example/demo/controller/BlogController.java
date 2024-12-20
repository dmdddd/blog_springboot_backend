package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.example.demo.dto.BlogResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.BlogService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponseDto>> getAllBlogs() {

        List<BlogResponseDto> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs); // 200 OK with the articles
    }

    @GetMapping("/{blog_name}")
    public ResponseEntity<BlogResponseDto> getBlog(@PathVariable("blog_name") String blogName) {

        BlogResponseDto blog = blogService.getBlog(blogName);
        return ResponseEntity.ok(blog); // 200 OK with the articles
    }

    // @GetMapping("/articles/{article_name}")
    // public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable("article_name") String name) {

    //     ArticleResponseDto articleResponseDto = articleService.getArticleByName(name);
    //     return ResponseEntity.ok(articleResponseDto); // Return ArticleDTO with 200 OK
    // }

    // @PutMapping("/{article_name}/vote")
    // public ResponseEntity<ArticleResponseDto> voteOnArticle(@PathVariable("article_name") String articleName, @RequestParam("type") String voteType) {
    //     logger.info("Request vote type received: " + voteType);
    //     String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    //     ArticleResponseDto downdatedArticle = articleService.voteOnArticle(articleName, voteType, userId);
    //     return ResponseEntity.ok(downdatedArticle); // Return the updated article DTO with 200 OK
    // }

    // @GetMapping("/checkSlug/{slug}")
    // public ResponseEntity<Map<String, Boolean>> checkSlug(@PathVariable String slug) {
    //     boolean isUnique = articleService.existsBySlug(slug);
    //     return ResponseEntity.ok(Map.of("isUnique", isUnique));
    // }

    // @PostMapping
    // public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleRequestDto articleRequest) {
    //     logger.info("Received request to create a new article: {}", articleRequest.getTitle());

    //     ArticleResponseDto newArticle = articleService.createArticle(articleRequest);

    //     logger.info("Successfully added a new article with ID: {}", newArticle.getId());

    //     URI location = ServletUriComponentsBuilder.fromCurrentRequest()
    //         .path("/{id}")
    //         .buildAndExpand(newArticle.getId())
    //         .toUri();

    //     return ResponseEntity.created(location).body(newArticle);
    // }

    // @GetMapping("/{article_name}/comments")
    // public ResponseEntity<List<CommentResponseDto>> getAllCommentsOfArticle(@PathVariable("article_name") String articleName) {
    //     logger.info("Received request to get all comments for article: {}", articleName);

    //     List<CommentResponseDto> comments = commentService.getAllCommentsOfArticle(articleName);
    //     logger.info("Found {} comments for article: {}", comments.size(), articleName);
    //     return ResponseEntity.ok(comments); // 200 OK with the comments
    // }

    // @PostMapping("/{article_name}/comments")
    // public ResponseEntity<CommentResponseDto> addCommentToArticle(
    //                                     @PathVariable("article_name") String articleName,
    //                                     @RequestBody CommentRequestDto commentRequest) {

    //     logger.info("Received request to add a comment to article {}", articleName);
    //     CommentResponseDto responseDTO = commentService.createComment(commentRequest, articleName);
    //     logger.info("Successfully added comment to article {}", articleName);

    //     URI location = ServletUriComponentsBuilder.fromCurrentRequest()
    //         .path("/{id}")
    //         .buildAndExpand(responseDTO.get_id())
    //         .toUri();

    //     return ResponseEntity.created(location).body(responseDTO);
    // }
}

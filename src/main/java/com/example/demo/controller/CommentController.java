package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.dto.PhotoUrlUpdateRequestDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("/api/blogs/{blog_name}/articles/{article_name}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByBlogByArticle(@PathVariable("blog_name") String blog, @PathVariable("article_name") String article) {
        logger.info("Received request to get all comments for blog: {} for article: {}", blog, article);

        List<CommentResponseDto> comments = commentService.getCommentsByBlogAndArticle(blog, article);
        logger.info("Found {} comments for article: {}", comments.size(), article);
        return ResponseEntity.ok(comments); // 200 OK with the comments
    }

    @PostMapping("/api/blogs/{blog_name}/articles/{article_name}/comments")
    public ResponseEntity<CommentResponseDto> addCommentToArticle(
                                        @PathVariable("blog_name") String blog,
                                        @PathVariable("article_name") String article,
                                        @RequestBody CommentRequestDto commentRequest) {

        logger.info("Received request to add a comment to article: {} of blog: {}", article, blog);
        CommentResponseDto responseDTO = commentService.createComment(commentRequest, blog, article);
        logger.info("Successfully added comment to article {}", article);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseDTO.get_id())
            .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @DeleteMapping("/api/comments/{comment_id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("comment_id") String commentId) {

        logger.info("Received request to delete comment id {}", commentId);
        commentService.deleteCommentById(commentId);
        logger.info("Successfully deleted comment id {}", commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/comments/{comment_id}")
    public ResponseEntity<CommentResponseDto> editComment(
                                        @PathVariable("comment_id") String id,
                                        @RequestBody CommentRequestDto commentRequest){
        logger.info("Received reqest to update comment id {}", id);
        CommentResponseDto updatedComment = commentService.updateCommentText(id, commentRequest.getText());
        logger.info("Successfully updated comment id {}", id);
        return ResponseEntity.ok(updatedComment); // Return the response DTO back to the client
    }

    @PostMapping("/api/comments/updateIcon")
    public ResponseEntity<String> updatePhotoUrl(@RequestBody PhotoUrlUpdateRequestDto requestDto) {

        logger.info("Received reqest to update icon for comments of user");
        commentService.updatePhotoUrlForUser(requestDto.getPhotoURL());
        logger.info("Successfully updated icon for comments of user");
        return ResponseEntity.ok("Comments updated successfully");
    }
}

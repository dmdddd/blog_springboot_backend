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
@RequestMapping("/api/comments/")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private CommentService commentService;
    

    // GET endpoint to retrieve all comments on an article
    @GetMapping("/{article_name}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsOfArticle(@PathVariable("article_name") String articleName) {
        logger.info("Received request to get all comments for article: {}", articleName);

        List<CommentResponseDto> comments = commentService.getAllCommentsOfArticle(articleName);
        logger.info("Found {} comments for article: {}", comments.size(), articleName);
        return ResponseEntity.ok(comments); // 200 OK with the comments
    }

    @PostMapping("/add/{article_name}")
    public ResponseEntity<CommentResponseDto> addCommentToArticle(
                                        @PathVariable("article_name") String articleName,
                                        @RequestBody CommentRequestDto commentRequest) {

        logger.info("Received request to add a comment to article {}", articleName);
        CommentResponseDto responseDTO = commentService.createComment(commentRequest, articleName);
        logger.info("Successfully added comment to article {}", articleName);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseDTO.get_id())
            .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @DeleteMapping("/delete/{comment_id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("comment_id") String commentId) {

        logger.info("Received request to delete comment id {}", commentId);
        commentService.deleteCommentById(commentId);
        logger.info("Successfully deleted comment id {}", commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @PutMapping("/edit/{comment_id}")
    public ResponseEntity<CommentResponseDto> editComment(
                                        @PathVariable("comment_id") String id,
                                        @RequestBody CommentRequestDto updatedComment){
        logger.info("Received reqest to update comment id {}", id);
        CommentResponseDto responseDTO = commentService.updateCommentText(id, updatedComment.getText());
        logger.info("Successfully updated comment id {}", id);
        return ResponseEntity.ok(responseDTO); // Return the response DTO back to the client
    }

    @PostMapping("/updateIcon")
    public ResponseEntity<String> updatePhotoUrl(@RequestBody PhotoUrlUpdateRequestDto requestDto) {

        logger.info("Received reqest to update icon for comments of user");
        commentService.updatePhotoUrlForUser(requestDto.getPhotoURL());
        logger.info("Successfully updated icon for comments of user");
        return ResponseEntity.ok("Comments updated successfully");
    }
}

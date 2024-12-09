package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable("comment_id") String commentId) {

        logger.info("Received request to delete comment id {}", commentId);
        commentService.deleteCommentById(commentId);
        logger.info("Successfully deleted comment id {}", commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDto> editComment(
                                        @PathVariable("comment_id") String id,
                                        @RequestBody CommentRequestDto commentRequest){
        logger.info("Received reqest to update comment id {}", id);
        CommentResponseDto updatedComment = commentService.updateCommentText(id, commentRequest.getText());
        logger.info("Successfully updated comment id {}", id);
        return ResponseEntity.ok(updatedComment); // Return the response DTO back to the client
    }

    @PostMapping("/updateIcon")
    public ResponseEntity<String> updatePhotoUrl(@RequestBody PhotoUrlUpdateRequestDto requestDto) {

        logger.info("Received reqest to update icon for comments of user");
        commentService.updatePhotoUrlForUser(requestDto.getPhotoURL());
        logger.info("Successfully updated icon for comments of user");
        return ResponseEntity.ok("Comments updated successfully");
    }
}

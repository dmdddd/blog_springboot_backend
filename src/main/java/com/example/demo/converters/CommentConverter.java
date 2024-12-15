package com.example.demo.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.CommentResponseDto;
import com.example.demo.model.Comment;

@Component
public class CommentConverter {
    public CommentResponseDto toDto(Comment comment, String email) {

        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }

        return new CommentResponseDto(
            comment.getId(), 
            comment.getPostedBy(), 
            comment.getText(), 
            comment.getBlog(),
            comment.getArticleName(), 
            comment.getUserEmail(), 
            comment.getUserIcon(), 
            comment.getCreatedOn(), 
            canUserDeleteComment(comment, email)
        );
    }

     public List<CommentResponseDto> toDtoList(List<Comment> comments, String email) {

        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList(); // Return empty list if input is null or empty
        }

        return comments.stream()
                .map(comment -> toDto(comment, email)) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    private boolean canUserDeleteComment(Comment comment, String email) {
        return comment.getUserEmail().equals(email);
        // return comment.getUserEmail().equals(currentUserEmail) || isAdmin(currentUserEmail);
    }
}

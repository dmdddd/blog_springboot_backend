package com.example.demo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.converters.CommentConverter;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.exceptions.ArticleNotFoundException;
import com.example.demo.exceptions.CommentNotFoundException;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.model.Comment;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final CommentConverter commentConverter;
    private final AuthenticationService authenticationService;

    // Constructor injection
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, CommentConverter commentConverter, AuthenticationService authenticationService) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.commentConverter = commentConverter;
        this.authenticationService = authenticationService;
    }

    public List<CommentResponseDto> getAllCommentsOfArticle(String articleName) {
        List<Comment> comments = commentRepository.findByArticleName(articleName);
        // Return an empty list if no comments are found
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert the list of comments to a list of DTOs
        return comments.stream()
                .map(comment -> commentConverter.toDto(comment, authenticationService.getCurrentUserEmail()))
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> addComment(CommentRequestDto commentRequest, String article) {

        // Check if the article exists
        logger.debug("Checking if article with name {} exists", article);
        if (!articleRepository.existsByName(article)) {
            throw new ArticleNotFoundException("Article with name " + article + " not found");
        }

        Comment newComment = new Comment(
            null, // ID will be auto-generated
            authenticationService.getCurrentUserName(),   // name of the user, for example "Dan"
            commentRequest.getText(),
            article,
            authenticationService.getCurrentUserEmail(),
            authenticationService.getCurrentUserPhotoUrl(),
            new Date() // Current timestamp
        );

        logger.debug("Saving new comment {}", newComment);
        commentRepository.save(newComment);

        logger.info("Successfully saved comment with ID {} for article {}", newComment.getId(), article);
        List<CommentResponseDto> comments = getAllCommentsOfArticle(article);
        return comments;
    }

    public void deleteCommentById(String id) {

        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
        commentRepository.deleteById(id);
        logger.info("Successfully deleted comment with ID {}", id);
    }

    public CommentResponseDto updateCommentText(String id, String text) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }

        Comment comment = existingComment.get();
        comment.setText(text);
        commentRepository.save(comment);

        return commentConverter.toDto(comment, authenticationService.getCurrentUserEmail());
    }
}

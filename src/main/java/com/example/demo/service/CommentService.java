package com.example.demo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.converters.CommentConverter;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final AuthenticationService authenticationService;

    // Constructor injection
    public CommentService(CommentRepository commentRepository, CommentConverter commentConverter, AuthenticationService authenticationService) {
        this.commentRepository = commentRepository;
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

        // TODO: check if article exists
        logger.debug("Checking if article with ID {} exists", article);
        //////////
        /// // Check if article exists
        // Article article = articleService.getArticleById(articleId);
        // if (article == null) {
        //     logger.warn("Article with ID {} not found", articleId);
        //     throw new ResourceNotFoundException("Article not found");
        // }


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
}

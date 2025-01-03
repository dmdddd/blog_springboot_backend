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
import com.example.demo.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final CommentRepository commentRepository;
    private final ArticleCommentService articleCommentService;
    private final CommentConverter commentConverter;
    private final AuthenticationService authenticationService;
    private final CommentCountUpdateService commentCountUpdateService;

    // Constructor injection
    public CommentService(CommentRepository commentRepository, ArticleCommentService articleCommentService, CommentConverter commentConverter, AuthenticationService authenticationService, CommentCountUpdateService commentCountUpdateService) {
        this.commentRepository = commentRepository;
        this.articleCommentService = articleCommentService;
        this.commentConverter = commentConverter;
        this.authenticationService = authenticationService;
        this.commentCountUpdateService = commentCountUpdateService;
    }

    public List<CommentResponseDto> getCommentsByBlogAndArticle(String blog, String articleName) {
        logger.info("Fetching comments for article: {}", articleName);

        List<Comment> comments = commentRepository.findByBlogAndArticleName(blog, articleName);
        // Return an empty list if no comments are found
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        logger.info("Found {} comments for article: {}", comments.size(), articleName);

        // Convert the list of comments to a list of DTOs
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        List<CommentResponseDto> commentDtos = comments.stream()
            .map(comment -> {
                logger.debug("Converting comment with ID {} for user {}", comment.getId(), currentUserEmail);
                return commentConverter.toDto(comment, currentUserEmail);
            })
            .collect(Collectors.toList());


        logger.info("Successfully converted comments for article: {}", articleName);
        return commentDtos; 

    }

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequest, String blog, String article) {

        // Check if the article exists
        logger.debug("Checking if article with name {} exists in blog {}", article, blog);
        if (!articleCommentService.articleExists(blog, article)) {
            throw new ArticleNotFoundException("Article with name " + article + " not found in blog " + blog);
        }

        Comment newComment = new Comment(

            null, // ID will be auto-generated
            authenticationService.getCurrentUserName(),   // name of the user, for example "Dan"
            commentRequest.getText(),
            blog,
            article,
            authenticationService.getCurrentUserEmail(),
            authenticationService.getCurrentUserPhotoUrl(),
            new Date(),
            new Date()
        );

        logger.debug("Saving new comment {}", newComment);
        commentRepository.save(newComment);

        logger.info("Successfully saved comment with ID {} for article {}", newComment.getId(), article);

        commentCountUpdateService.incrementCommentCount(blog, article, 1);

        return commentConverter.toDto(newComment, authenticationService.getCurrentUserEmail());
    }

    @Transactional
    public void deleteComment(String blog, String article, String id) {

        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
        commentRepository.deleteById(id);
        commentCountUpdateService.decrementCommentCount(blog, article, 1);
        logger.info("Successfully deleted comment with ID {}", id);
    }

    public CommentResponseDto updateCommentText(String blog, String article, String id, String text) {

        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }

        Comment comment = existingComment.get();
        comment.setText(text);
        comment.setUpdatedAt(new Date());
        commentRepository.save(comment);

        return commentConverter.toDto(comment, authenticationService.getCurrentUserEmail());
    }

    public void updatePhotoUrlForUser(String photoUrl) {
        String email = authenticationService.getCurrentUserEmail();
        logger.info("Updating photoUrl for comments by {} to {}", email, photoUrl);
        long updated = commentRepository.updatePhotoUrlForEmail(email, photoUrl);
        logger.info("Successfully updated photoUrl for  {} comments by {} to {}", updated, email, photoUrl);
    }
}

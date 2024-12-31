package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.exceptions.InvalidSlugFormatException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.validation.SlugValidator;

@Service
public class ArticleCommentService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleCommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public boolean articleExists(String blog, String slug) {
        if (!SlugValidator.isValidSlug(slug)) {
            throw new InvalidSlugFormatException("Invalid slug format for: " + slug);
        }

        return articleRepository.existsByBlogAndName(blog, slug);
    }

    public void updateCommentsArticleName(String blog, String oldName, String newName) {
        logger.info("Updating article name for comments, from {} to {}, blog: {}", oldName, newName, blog);
        long updated = commentRepository.updateCommentsArticleName(blog, oldName, newName);
        logger.info("Successfully updated article name for {} comments, from {} to {}, blog: {}", updated, oldName, newName, blog);
    }
}

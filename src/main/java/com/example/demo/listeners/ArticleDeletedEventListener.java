package com.example.demo.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.events.ArticleDeletedEvent;
import com.example.demo.exceptions.CommentDeletionException;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.ArticleCommentService;

@Component
public class ArticleDeletedEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ArticleCommentService articleCommentService;

    public ArticleDeletedEventListener(ArticleCommentService articleCommentService) {
        this.articleCommentService = articleCommentService;
    }

    @Async
    @Retryable(value = { CommentDeletionException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    @EventListener
    public void onArticleDeleted(ArticleDeletedEvent event) {
        try {
            articleCommentService.deleteCommentsForArticle(event.getBlog(), event.getArticle());
        } catch (Exception e) {
            logger.error("Error deleting comments for blog {} article {}, retrying...", event.getBlog(), event.getArticle(), e);
            throw new CommentDeletionException("Failed to delete comments for article", e);
        }
    }
}

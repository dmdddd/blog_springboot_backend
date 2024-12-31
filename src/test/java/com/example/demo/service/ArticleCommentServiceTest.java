package com.example.demo.service;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;

@SpringBootTest
public class ArticleCommentServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository; // Mock the article repository

    @MockBean
    private ArticleCommentService articleCommentService; // Mock the ArticleCommentService

    @MockBean
    private CommentRepository commentRepository; // Mock the CommentRepository
        


    @BeforeEach
    public void setup() {
        // Manually mock authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "testUser", 
            null, 
            new ArrayList<>()  // No authorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication
        
        );
    }
    @Test
    public void testDeleteCommentsWhenArticleDeleted() {
        // Given
        String blogId = "web-design"; // Example articleId
        String articleId = "mongodb"; // Example articleId

        // Use AtomicBoolean to track if the async call is made
        AtomicBoolean methodCalled = new AtomicBoolean(false);
        doAnswer(invocation -> {
            methodCalled.set(true);
            return null;
        }).when(articleCommentService).deleteCommentsForArticle(blogId, articleId);

        Article mockArticle = new Article();
        mockArticle.setBlog(blogId);
        mockArticle.setName(articleId);
        mockArticle.setBlogAdminIds(Collections.singletonList("testUser"));

        when(articleRepository.findByBlogAndName(blogId, articleId)).thenReturn(java.util.Optional.of(mockArticle));

        articleService.deleteArticle(blogId, articleId);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilTrue(methodCalled);

        // Assert: Verify that the comments for the article have been deleted via ArticleCommentService
        verify(articleCommentService, times(1)).deleteCommentsForArticle(blogId, articleId);

        // Verify that the article is deleted from the repository
        verify(articleRepository, times(1)).findByBlogAndName(blogId, articleId);
        verify(articleRepository, times(1)).deleteByBlogAndName(blogId, articleId);

    }
}
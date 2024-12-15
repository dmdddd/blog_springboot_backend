package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.converters.ArticleConverter;
import com.example.demo.dto.ArticleRequestDto;
import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.exceptions.ArticleNotFoundException;
import com.example.demo.exceptions.DuplicateArticleException;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.exceptions.InvalidSlugFormatException;
import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.validation.SlugValidator;


@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    // Constructor injection
    public ArticleService(ArticleRepository articleRepository, ArticleConverter articleConverter) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
    }

    public List<ArticleResponseDto> getArticlesByBlog(String blog) {
       
        List<Article> articles = articleRepository.findByBlog(blog);

        // Return an empty list if no comments are found
        if (articles.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert the list of articles to a list of DTOs
        return articles.stream()
                .map(articleConverter::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto findByBlogAndName(String blog, String name) {
        Optional<Article> articleOptional = articleRepository.findByBlogAndName(blog, name);

        // Handle Optional, either return DTO or throw exception if article not found
        return articleOptional
                .map(articleConverter::toDto) // Convert Article to DTO if present
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with name: " + name)); // Throw exception if empty
    }

    public ArticleResponseDto voteOnArticle(String blog, String articleName, String voteType, String userId) {

        // Fetch the article by name from the repository
        Article article = articleRepository.findByBlogAndName(blog, articleName)
            .orElseThrow(() -> new ArticleNotFoundException("Article not found with name: " + articleName));

        logger.info("User {} is trying to {}vote on article: {}", userId, voteType, articleName);

        if (voteType.equalsIgnoreCase("up")) {
            return handleUpvote(article, userId);
        } else if (voteType.equalsIgnoreCase("down")) {
            return handleDownvote(article, userId);
        } else {
            throw new IllegalArgumentException("Invalid vote type: " + voteType);
        }
    }

    private ArticleResponseDto handleUpvote(Article article, String userId) {
        // Check if the user has already upvoted
        if (article.getUpvoteIds().contains(userId)) {
            throw new IllegalStateException("User has already upvoted this article.");
        }
    
        // Add user to the upvote list and increment upvotes
        article.getUpvoteIds().add(userId);
        article.setUpvotes(article.getUpvotes() + 1);
    
        return saveAndConvertArticle(article);
    }
    
    private ArticleResponseDto handleDownvote(Article article, String userId) {
        // Check if the user has not upvoted the article (i.e., can only downvote if previously upvoted)
        if (!article.getUpvoteIds().contains(userId)) {
            throw new IllegalStateException("User has not upvoted this article, cannot downvote.");
        }
    
        // Remove user from the upvote list and decrement upvotes
        article.getUpvoteIds().remove(userId);
        article.setUpvotes(article.getUpvotes() - 1);
    
        return saveAndConvertArticle(article);
    }

    private ArticleResponseDto saveAndConvertArticle(Article article) {
        Article updatedArticle = articleRepository.save(article);
        return articleConverter.toDto(updatedArticle);
    }

    public boolean existsByBlogAndSlug(String blog, String slug) {

        if (!SlugValidator.isValidSlug(slug)) {
            throw new InvalidSlugFormatException("Invalid slug format for: " + slug);

        }

        return !articleRepository.existsByBlogAndName(blog, slug);
    }

    public ArticleResponseDto createArticle(ArticleRequestDto articleRequest) {
        logger.debug("Checking if slug is valid for article {}", articleRequest.getName());

        if (!SlugValidator.isValidSlug(articleRequest.getName())) {
            throw new InvalidSlugFormatException("Invalid slug format for article: " + articleRequest.getName());
        }

        if (articleRepository.existsByBlogAndName(articleRequest.getBlog(), articleRequest.getName())) {
            logger.warn("Attempted to create an article with a duplicate slug: {}", articleRequest.getName());
            throw new DuplicateArticleException("An article with the slug '" + articleRequest.getName() + "' already exists.");
        }

        Article newArticle = new Article(null, articleRequest.getName(), articleRequest.getBlog(), articleRequest.getTitle(),
            Arrays.asList(articleRequest.getText().split("\n")), 0, new ArrayList<String>());


        logger.debug("Saving new article {}", newArticle);

        articleRepository.save(newArticle);
        logger.info("Successfully saved article with ID: {} for blog: {}", newArticle.getId(), "");

        return articleConverter.toDto(newArticle);

    }
}

package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.converters.ArticleConverter;
import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.exceptions.ArticleNotFoundException;
import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;

    // Constructor injection
    public ArticleService(ArticleRepository articleRepository, ArticleConverter articleConverter) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
    }

    public List<ArticleResponseDto> getAllArticles() {
       
        List<Article> articles = articleRepository.findAll();

        // Return an empty list if no articles are found
        if (articles.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert the list of articles to a list of DTOs
        return articles.stream()
                .map(articleConverter::toDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getArticleByName(String name) {
        Optional<Article> articleOptional = articleRepository.findByName(name);

        // Handle Optional, either return DTO or throw exception if article not found
        return articleOptional
                .map(articleConverter::toDto) // Convert Article to DTO if present
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with name: " + name)); // Throw exception if empty
    }

    // public Article saveArticle(Article article) {
    //     validateArticle(article);
    //     return articleRepository.save(article); // Uses the default `save` method
    // }

    // private void validateArticle(Article article) {
    //     if (article.getName() == null || article.getName().isEmpty()) {
    //         throw new IllegalArgumentException("Article name cannot be null or empty");
    //     }
    // }

    public ArticleResponseDto upvoteArticle(String articleName, String userId) {

        // Fetch the article by name from the repository
        Optional<Article> articleOptional = articleRepository.findByName(articleName);

        // Handle the absence of the article
        if (articleOptional.isEmpty()) {
            throw new ArticleNotFoundException("Article not found with name: " + articleName);
        }

        // Extract the Article object from the Optional
        Article article = articleOptional.get();

        // Check if the user has already upvoted
        if (article.getUpvoteIds().contains(userId)) {
            throw new IllegalStateException("User has already upvoted this article.");
        }

        article.getUpvoteIds().add(userId);             // Add the user ID to the upvote list
        article.setUpvotes(article.getUpvotes() + 1);   // Increment upvotes

        // Save the updated article to the repository
        Article updatedArticle = articleRepository.save(article);

        // Convert the updated article to a DTO and return it
        return articleConverter.toDto(updatedArticle);
    }

    public ArticleResponseDto downvoteArticle(String articleName, String userId) {

        // Fetch the article by name from the repository
        Optional<Article> articleOptional = articleRepository.findByName(articleName);

        // Handle the absence of the article
        if (articleOptional.isEmpty()) {
            throw new ArticleNotFoundException("Article not found with name: " + articleName);
        }

        // Extract the Article object from the Optional
        Article article = articleOptional.get();

        // Check if the user has already upvoted
        if (!article.getUpvoteIds().contains(userId)) {
            throw new IllegalStateException("User has already downvoted this article.");
        }
        
        article.getUpvoteIds().remove(userId);             // Remove the user ID to the upvote list
        article.setUpvotes(article.getUpvotes() - 1);      // Decrement upvotes

        // Save the updated article to the repository
        Article updatedArticle = articleRepository.save(article);

        // Convert the updated article to a DTO and return it
        return articleConverter.toDto(updatedArticle);
    }
}

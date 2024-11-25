package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    // Constructor injection
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleByName(String name) {
        return articleRepository.findByName(name);
    }

    public Article saveArticle(Article article) {
        validateArticle(article);
        return articleRepository.save(article); // Uses the default `save` method
    }

    private void validateArticle(Article article) {
        if (article.getName() == null || article.getName().isEmpty()) {
            throw new IllegalArgumentException("Article name cannot be null or empty");
        }
    }

    public Optional<Article> upvoteArticle(String articleName) {
        Optional<Article> articleOpt = articleRepository.findByName(articleName);
        if (!articleOpt.isPresent()) {
            return Optional.empty(); // Article not found
        }

        Article article = articleOpt.get();
        article.setUpvotes(article.getUpvotes() + 1);   // Increment upvotes
        articleRepository.save(article);                // Save updated article
        return Optional.of(article);
    }

    public Optional<Article> downvoteArticle(String articleName) {
        Optional<Article> articleOpt = articleRepository.findByName(articleName);
        if (!articleOpt.isPresent()) {
            return Optional.empty(); // Article not found
        }
        
        Article article = articleOpt.get();
        article.setUpvotes(article.getUpvotes() - 1);   // Increment upvotes
        articleRepository.save(article);                // Save updated article
        return Optional.of(article);
    }
}

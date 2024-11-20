package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll(); // Uses the default `findAll` method
    }

    public Optional<Article> getArticleByName(String name) {
        return articleRepository.findByName(name);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article); // Uses the default `save` method
    }
}

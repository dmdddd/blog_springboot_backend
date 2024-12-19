package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends MongoRepository<Article, String> {
    List<Article> findByBlog(String blogName);
    Optional<Article> findByBlogAndName(String blogName, String articleName);
    boolean existsByBlogAndName(String blog, String name);
    void deleteByBlogAndName(String blog, String name);
}
package com.example.demo.repository;

import com.example.demo.model.ArticleCommentChange;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleCommentChangeRepository extends MongoRepository<ArticleCommentChange, String> {

    Optional<ArticleCommentChange> findByBlogSlugAndArticleSlug(String blogSlug, String articleSlug);
}
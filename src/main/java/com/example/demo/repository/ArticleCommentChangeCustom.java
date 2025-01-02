package com.example.demo.repository;

public interface ArticleCommentChangeCustom {
    void incrementNetChange(String blogSlug, String articleSlug, int delta);
}

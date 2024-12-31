package com.example.demo.events;

import lombok.Data;

@Data
public class ArticleDeletedEvent {
    private final String blog;
    private final String article;
}
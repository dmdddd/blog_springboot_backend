package com.example.demo.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ArticleResponseDto;
import com.example.demo.model.Article;

@Component
public class ArticleConverter {

    /**
     * Converts an Article entity to a DTO.
     * 
     * @param article the article to convert, must not be null
     * @return the converted ArticleResponseDto
     * @throws IllegalArgumentException if the article is null
     */
    public ArticleResponseDto toDto(Article article) {

        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ArticleResponseDto(article.getId(), article.getName(), article.getTitle(), article.getContent(), article.getUpvotes(), article.getUpvoteIds(), useCanUpvoteArticle(article, userId));
    }

    /**
     * Converts a list of Article entities to a list of DTOs.
     * 
     * @param articles the list of articles to convert, can be null or empty
     * @return a list of converted ArticleResponseDto, or an empty list if input is null or empty
     */
    public List<ArticleResponseDto> toDtoList(List<Article> articles) {

        if (articles == null || articles.isEmpty()) {
            return Collections.emptyList(); // Return empty list if input is null or empty
        }

        return articles.stream()
                .map(article -> toDto(article)) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    private boolean useCanUpvoteArticle(Article article, String userId) {
        return !article.getUpvoteIds().contains(userId);
    }
}

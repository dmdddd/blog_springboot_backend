package com.example.demo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;


public class ArticleServiceTest_getAllArticles {

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        // Initialize mocks and inject them into the service
        MockitoAnnotations.openMocks(this);
        articleService = new ArticleService(articleRepository);
    }

    @Test
    void getAllArticles_shouldReturnAllArticles() {
        // Arrange: Set up mock data
        Article article1 = new Article(
                "1",
                "Article 1",
                "Getting Started with Spring Boot",
                Arrays.asList("This is a detailed article about Spring Boot basics."),
                25,
                Arrays.asList("user1", "user2")
        );
        Article article2 = new Article(
                "2",
                "Article 2",
                "Getting Started with Spring Boot",
                Arrays.asList("This is a detailed article about Spring Boot basics."),
                25,
                Arrays.asList("user1", "user3")
        );

        List<Article> mockArticles = Arrays.asList(article1, article2);

        when(articleRepository.findAll()).thenReturn(mockArticles);

        // Act: Call the method under test
        List<Article> articles = articleService.getAllArticles();

        // Assert: Verify the results
        assertNotNull(articles);
        assertEquals(2, articles.size());
        assertEquals("Article 1", articles.get(0).getName());
        assertEquals("Article 2", articles.get(1).getName());

        // Verify that the repository's findAll() method was called exactly once
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void getAllArticles_shouldReturnEmptyListWhenNoArticlesExist() {
        // Arrange: Mock the repository to return an empty list
        when(articleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act: Call the method
        List<Article> articles = articleService.getAllArticles();

        // Assert: Verify the result
        assertNotNull(articles);
        assertTrue(articles.isEmpty());

        // Verify repository interaction
        verify(articleRepository, times(1)).findAll();
    }

    @ParameterizedTest
    @CsvSource({
        "1, article 1, Article One, Content 1, 0, 'dan'",
        "2, article 2, Article Two, Content 2, 56, 'd,1'",
        "3, article 3, Article Three, Content 3, 669, '1,3'"
    })
    void getAllArticles_shouldReturnCorrectArticle(String id, String name, String title, List<String> content, int upvotes, String upvoteIds) {
        // Arrange
        Article article = new Article(id, name, title, content, upvotes, List.of(upvoteIds.split(",")));
        List<Article> mockArticles = Arrays.asList(article);

        when(articleRepository.findAll()).thenReturn(mockArticles);

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert
        assertTrue(articles.size() == 1);
        assertEquals(name, articles.get(0).getName());
        assertEquals(content, articles.get(0).getContent());
        assertEquals(title, articles.get(0).getTitle());
        assertEquals(upvotes, articles.get(0).getUpvotes());
        assertEquals(upvoteIds, String.join(",", articles.get(0).getUpvoteIds()));
}

    // @Test
    // void getAllArticles_shouldHandleNullResponseFromRepository() {
    //     // Arrange: Mock the repository to return null
    //     when(articleRepository.findAll()).thenReturn(null);

    //     // Act: Call the method
    //     List<Article> articles = articleService.getAllArticles();

    //     // Assert: Ensure it returns an empty list (assuming that's your desired behavior)
    //     assertNotNull(articles);
    //     assertTrue(articles.isEmpty());

    //     // Verify repository interaction
    //     verify(articleRepository, times(1)).findAll();
    // }

    @Test
    void getAllArticles_shouldCallRepositoryFindAllOnce() {
        // Arrange
        when(articleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        articleService.getAllArticles();

        // Assert: Verify interaction with the repository
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void getAllArticles_shouldReturnArticlesInCorrectOrder() {
        // Arrange: Create mock articles
        Article article1 = new Article("1", "First Article", "Title 1", Arrays.asList("Content 1"), 5, Collections.emptyList());
        Article article2 = new Article("2", "Second Article", "Title 2", Arrays.asList("Content 2"), 10, Collections.emptyList());

        when(articleRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert: Verify the order
        assertEquals("First Article", articles.get(0).getName());
        assertEquals("Second Article", articles.get(1).getName());
    }

    @Test
    void getAllArticles_shouldHandleLargeDataSets() {
        // Arrange: Create a large number of mock articles
        List<Article> largeDataSet = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            largeDataSet.add(new Article(String.valueOf(i), "Article " + i, "Title " + i, Arrays.asList("Content " + i), i, Collections.emptyList()));
        }

        when(articleRepository.findAll()).thenReturn(largeDataSet);

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert: Verify the size and data
        assertEquals(1000, articles.size());
        assertEquals("Article 1", articles.get(0).getName());
        assertEquals("Article 1000", articles.get(999).getName());
    }

    @Test
    void getAllArticles_shouldHandleRepositoryException() {
        // Arrange: Mock repository to throw an exception
        when(articleRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.getAllArticles();
        });

        assertEquals("Database error", exception.getMessage());

        // Verify repository interaction
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void getAllArticles_shouldHandleSpecialCharactersInArticles() {
        // Arrange: Create mock articles with special characters
        Article article = new Article("1", "Art!cle @#$", "Titl€%", Arrays.asList("Cøntênt*"), 10, Collections.emptyList());

        when(articleRepository.findAll()).thenReturn(Collections.singletonList(article));

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert
        assertEquals(1, articles.size());
        assertEquals("Art!cle @#$", articles.get(0).getName());
        assertEquals("Titl€%", articles.get(0).getTitle());
        assertEquals("Cøntênt*", articles.get(0).getContent());
    }

}

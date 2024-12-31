package com.example.demo.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.BlogResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.model.Blog;
import com.example.demo.model.BlogUser.BlogRole;

@Component
public class BlogConverter {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Converts an Blog entity to a DTO.
     * 
     * @param blog the blog to convert, must not be null
     * @return the converted BlogResponseDto
     * @throws IllegalArgumentException if the article is null
     */
    public BlogResponseDto toDto(Blog blog) {

        if (blog == null) {
            throw new IllegalArgumentException("Blog cannot be null");
        }
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isAdmin = blog.getUsers().stream()
            .anyMatch(user -> user.getUid().equals(userId) && user.getRole() == BlogRole.ADMIN);
        boolean isEditor = blog.getUsers().stream()
            .anyMatch(user -> user.getUid().equals(userId) && user.getRole() == BlogRole.EDITOR);

        return new BlogResponseDto(blog.getId(), blog.getName(), blog.getTitle(), blog.getDescription(), blog.getCreatedAt(), blog.getUpdatedAt(), isAdmin, isEditor);
    }

    /**
     * Converts a list of Blog entities to a list of DTOs.
     * 
     * @param blog the list of articles to convert, can be null or empty
     * @return a list of converted BlogResponseDto, or an empty list if input is null or empty
     */
    public List<BlogResponseDto> toDtoList(List<Blog> blogs) {

        if (blogs == null || blogs.isEmpty()) {
            return Collections.emptyList(); // Return empty list if input is null or empty
        }

        return blogs.stream()
                .map(blog -> toDto(blog)) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }
}

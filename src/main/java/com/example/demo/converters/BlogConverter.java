package com.example.demo.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.BlogResponseDto;
import com.example.demo.model.Blog;

@Component
public class BlogConverter {

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

        return new BlogResponseDto(blog.getId(), blog.getName(), blog.getTitle(), blog.getDescription(), blog.getCreatedOn(), blog.getUsers());
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

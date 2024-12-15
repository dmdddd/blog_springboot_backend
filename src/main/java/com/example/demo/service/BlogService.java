package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.converters.BlogConverter;
import com.example.demo.dto.BlogResponseDto;
import com.example.demo.exceptions.BlogNotFoundException;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.model.Blog;
import com.example.demo.repository.BlogRepository;

@Service
public class BlogService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final BlogRepository blogRepository;
    private final BlogConverter blogConverter;
    private final AuthenticationService authenticationService;

    // Constructor injection
    public BlogService(BlogRepository blogRepository, BlogConverter blogConverter, AuthenticationService authenticationService) {
        this.blogRepository = blogRepository;
        this.blogConverter = blogConverter;
        this.authenticationService = authenticationService;
    }

    public List<BlogResponseDto> getAllBlogs() {
        
        List<Blog> blogs = blogRepository.findAll();

        // Return an empty list if no articles are found
        if (blogs.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert the list of articles to a list of DTOs
        return blogs.stream()
                .map(blogConverter::toDto)
                .collect(Collectors.toList());
    }

    public BlogResponseDto getBlog(String blog){
        Optional<Blog> optionalBlog = blogRepository.findByName(blog);
        
        return optionalBlog.map(blogConverter::toDto) // Convert Article to DTO if present
                .orElseThrow(() -> new BlogNotFoundException("Blog not found with name: " + blog)); // Throw exception if empty
    }
}
package com.example.demo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.converters.BlogConverter;
import com.example.demo.dto.BlogRequestDto;
import com.example.demo.dto.BlogResponseDto;
import com.example.demo.exceptions.BlogNotFoundException;
import com.example.demo.exceptions.DuplicateBlogException;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.exceptions.InvalidSlugFormatException;
import com.example.demo.model.Blog;
import com.example.demo.model.BlogUser;
import com.example.demo.model.BlogUser.BlogRole;
import com.example.demo.repository.BlogRepository;
import com.example.demo.validation.SlugValidator;

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

    public Blog getBlog(String blog) {
        return blogRepository.findByName(blog)
            .orElseThrow(() -> new BlogNotFoundException("Blog not found with name: " + blog));
    }

    public BlogResponseDto getBlogDto(String blog){
        Blog blogEntity = getBlog(blog);
        return blogConverter.toDto(blogEntity);
    }

    public BlogResponseDto createBlog(BlogRequestDto blogRequest) {

        logger.debug("Checking if slug is valid for blog {}", blogRequest.getName());

        if (!SlugValidator.isValidSlug(blogRequest.getName())) {
            throw new InvalidSlugFormatException("Invalid slug format for article: " + blogRequest.getName());
        }

        if (blogRepository.existsByName(blogRequest.getName())) {
            throw new DuplicateBlogException("Blog with the name '" + blogRequest.getName() + "' already exists.");
        }

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogUser admin = new BlogUser(userId, authenticationService.getCurrentUserEmail(), authenticationService.getCurrentUserName(), BlogRole.ADMIN, new Date());
        Blog newBlog = new Blog(null, blogRequest.getName(), blogRequest.getTitle(), blogRequest.getDescription(), new Date(), Arrays.asList(admin));

        logger.debug("Saving new blog {}", newBlog);

        blogRepository.save(newBlog);

        logger.info("Successfully saved blog: {}", newBlog.getName());

        return blogConverter.toDto(newBlog);
    }

    public boolean uniqueName(String slug) {

        if (!SlugValidator.isValidSlug(slug)) {
            throw new InvalidSlugFormatException("Invalid slug format for: " + slug);
        }

        return !blogRepository.existsByName(slug);
    }

    public List<String> getAdmins(List<BlogUser> users) {
        List<String> admins = users.stream()
            .filter(user -> user.getRole() == BlogRole.ADMIN)
            .map(BlogUser::getUid)
            .collect(Collectors.toList());

        return admins;
    }

    public List<String> getEditors(List<BlogUser> users) {
        List<String> editors = users.stream()
            .filter(user -> user.getRole() == BlogRole.EDITOR)
            .map(BlogUser::getUid)
            .collect(Collectors.toList());

        return editors;
    }
}
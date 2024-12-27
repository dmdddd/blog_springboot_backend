package com.example.demo.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.converters.PageConverter;
import com.example.demo.dto.PageRequestDto;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.exceptions.InvalidRequestException;
import com.example.demo.exceptions.PageNotFoundException;
import com.example.demo.exceptions.SlugAlreadyExistsException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.model.Blog;
import com.example.demo.model.Page;
import com.example.demo.repository.PageRepository;

@Service
public class PageService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final PageRepository pageRepository;
    private final PageConverter pageConverter;
    private final BlogService blogService;
    private final AuthenticationService authenticationService;

    // Constructor injection
    public PageService(PageRepository pageRepository, PageConverter pageConverter, AuthenticationService authenticationService, BlogService blogService) {
        this.pageRepository = pageRepository;
        this.pageConverter = pageConverter;
        this.authenticationService = authenticationService;
        this.blogService = blogService;

    }

    public List<PageResponseDto> findPagesByBlog(String blog) {
        
        List<Page> pages = pageRepository.findByBlog(blog);

        // Return an empty list if no articles are found
        if (pages.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert the list of articles to a list of DTOs
        return pages.stream()
                .map(pageConverter::toDto)
                .collect(Collectors.toList());
    }

    public PageResponseDto updatePage(String blogName, String pageSlug, PageRequestDto requestDto) {

        logger.debug("Checking permissions for {}", blogName);
        Blog blog = blogService.getBlog(blogName);
        List<String> admins = blogService.getAdmins(blog.getUsers());
        List<String> editors = blogService.getEditors(blog.getUsers());

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!admins.contains(userId) && !editors.contains(userId)) {
            throw new UnauthorizedException("You don't have permission to edit pages in blog: " + blogName);
        }

        Optional<Page> existingPage = pageRepository.findByBlogAndSlug(blogName, pageSlug);
        if (existingPage.isEmpty()) {
            throw new PageNotFoundException("Page " + pageSlug + " not found in blog " + blogName);
        }

        Page page = existingPage.get();

        // Validate that title and slug are provided together or not at all
        if ((requestDto.getTitle() != null && requestDto.getSlug() == null) ||
            (requestDto.getTitle() == null && requestDto.getSlug() != null)) {
            throw new InvalidRequestException("Both title and slug must be provided together.");
        }

        if (!requestDto.getSlug().equals(page.getSlug())) { // Updating title and slug
            if (pageRepository.existsByBlogAndSlug(blogName, requestDto.getSlug())) {
                throw new SlugAlreadyExistsException("Slug '" + requestDto.getSlug() + "' already exists in blog '" + blogName + "'.");
            }
            page.setTitle(requestDto.getTitle());
            page.setSlug(requestDto.getSlug());
        }

        if (requestDto.getContent() != null) {
            page.setContent(requestDto.getContent());
        }

        page.setUpdatedAt(new Date());
        pageRepository.save(page);

        return pageConverter.toDto(page);
    }

    public void deletePageByBlogAndSlug(String blogName, String pageSlug) {

        logger.debug("Checking permissions for {}", blogName);
        Blog blog = blogService.getBlog(blogName);
        List<String> admins = blogService.getAdmins(blog.getUsers());

        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!admins.contains(userId)) {
            throw new UnauthorizedException("You don't have permission to delete pages in blog: " + blogName);
        }

        if (!pageRepository.existsByBlogAndSlug(blogName, pageSlug)) {
            throw new PageNotFoundException("Page '"+pageSlug+"' for blog '"+blogName+"' not found");
        }
        pageRepository.deleteByBlogAndSlug(blogName, pageSlug);
    }
    

}

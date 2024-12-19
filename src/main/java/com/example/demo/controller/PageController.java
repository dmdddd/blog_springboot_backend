package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PageRequestDto;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.service.PageService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/blogs/{blog_name}/pages")
public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private PageService pageService;

    @GetMapping
    public ResponseEntity<List<PageResponseDto>> getPagesByBlog(@PathVariable("blog_name") String blog) {

        List<PageResponseDto> pages = pageService.findPagesByBlog(blog);
        return ResponseEntity.ok(pages); // Return ArticleDTO with 200 OK
    }

    @PutMapping("/{page_slug}")
    public ResponseEntity<PageResponseDto> editPage(
                                        @PathVariable("blog_name") String blog,
                                        @PathVariable("page_slug") String page,
                                        @RequestBody PageRequestDto pageRequest){
        logger.info("Received reqest to update page '{}'' of blog: '{}'", page, blog);
        PageResponseDto updatedPage = pageService.updatePage(blog, page, pageRequest);
        logger.info("Successfully updated page '{}' of blog: '{}'", page, blog);
        return ResponseEntity.ok(updatedPage); // Return the response DTO back to the client
    }

}

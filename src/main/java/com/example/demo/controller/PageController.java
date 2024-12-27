package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PageRequestDto;
import com.example.demo.dto.PageResponseDto;
import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.exceptions.ValidationErrorResponse;
import com.example.demo.service.PageService;
import com.google.rpc.context.AttributeContext.Response;

import jakarta.validation.Valid;

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
    public ResponseEntity<?> editPage(
                                        @PathVariable("blog_name") String blog,
                                        @PathVariable("page_slug") String page,
                                        @Valid @RequestBody PageRequestDto pageRequest,
                                        BindingResult bindingResult) {

        logger.info("Update page [page={}, blog={}", page, blog);

        // DTO field validation
        if (bindingResult.hasErrors())
            return handleValidationErrors(bindingResult);

        PageResponseDto updatedPage = pageService.updatePage(blog, page, pageRequest);
        logger.info("Successfully updated page [page={}, blog={}", page, blog);

        return ResponseEntity.ok(updatedPage); // Return the response DTO back to the client
    }

    @DeleteMapping("/{page_slug}")
    public ResponseEntity<Void> deletePage(
        @PathVariable("blog_name") String blog,
        @PathVariable("page_slug") String page) {

            logger.info("Deleting page [page={}, blog={}]", page, blog);
            pageService.deletePageByBlogAndSlug(blog, page);
            logger.info("Successfully deleted page [page={}, blog={}]", page, blog);
            return ResponseEntity.noContent().build();

    }

    private ResponseEntity<ValidationErrorResponse> handleValidationErrors(BindingResult bindingResult) {

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        for (FieldError error : bindingResult.getFieldErrors()) {
            System.out.println(error.getField() + ", " + error.getDefaultMessage());
            validationErrorResponse.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(validationErrorResponse);
    }

}

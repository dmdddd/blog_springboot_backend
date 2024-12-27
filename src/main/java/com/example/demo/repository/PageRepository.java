package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Page;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends MongoRepository<Page, String> {
    List<Page> findByBlog(String blog);
    Optional<Page> findByBlogAndSlug(String blog, String slug);
    boolean existsByBlogAndSlug(String blog, String slug);
    void deleteByBlogAndSlug(String blog, String page);
}
package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Blog;

import java.util.Optional;

public interface BlogRepository extends MongoRepository<Blog, String> {
    Optional<Blog> findByName(String name);
    boolean existsByName(String name);
    // Page<Blog> findByCategory(String category, Pageable pageable);
    Page<Blog> findByTitleContaining(String search, Pageable pageable);
    // Page<Blog> findByCategoryAndTitleContaining(String category, String search, Pageable pageable);

}
package com.example.demo.converters;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.PageResponseDto;
import com.example.demo.model.Page;

@Component
public class PageConverter {
    public PageResponseDto toDto(Page page) {

        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }

        return new PageResponseDto(
            page.getId(), 
            page.getBlog(), 
            page.getSlug(), 
            page.getTitle(),
            page.getContent(), 
            page.getOrder(), 
            page.getCreatedAt(), 
            page.getUpdatedAt()
        );
    }

     public List<PageResponseDto> toDtoList(List<Page> pages) {

        if (pages == null || pages.isEmpty()) {
            return Collections.emptyList(); // Return empty list if input is null or empty
        }

        return pages.stream()
                .map(page -> toDto(page)) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

}

package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PaginatedResponseDto<T> {
    private List<T> data;                    // Generic type for data
    private PaginationMetadata pagination;   // Pagination metadata

    @Data
    @NoArgsConstructor
    public static class PaginationMetadata {
    private long totalItems;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrev;
    private Links links;

    @Data
    @NoArgsConstructor
    public static class Links {
        private String self;
        private String first;
        private String next;
        private String prev;
        private String last;
    }
}
}
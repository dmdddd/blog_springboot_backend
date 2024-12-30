package com.example.demo.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PaginatedResponseDto;

@Service
public class PaginationService {

    public Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        return PageRequest.of(page, size, sort);
    }

    public <T, R> PaginatedResponseDto<R> buildPaginationResponse(Page<T> pageResult, Function<T, R> converter, String baseUrl) {
        List<R> data = pageResult.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());

        PaginatedResponseDto<R> response = new PaginatedResponseDto<>();
        response.setData(data);

        PaginatedResponseDto.PaginationMetadata pagination = new PaginatedResponseDto.PaginationMetadata();
        int requestedPage  = pageResult.getNumber() + 1; // for user friendly interaction
        pagination.setTotalItems(pageResult.getTotalElements());
        pagination.setCurrentPage(requestedPage);
        pagination.setTotalPages(pageResult.getTotalPages());
        pagination.setPageSize(pageResult.getSize());
        pagination.setHasNext(pageResult.hasNext());
        pagination.setHasPrev(pageResult.hasPrevious());

        PaginatedResponseDto.PaginationMetadata.Links links = new PaginatedResponseDto.PaginationMetadata.Links();
        links.setSelf(baseUrl + "?page=" + requestedPage + "&size=" + pageResult.getSize());
        links.setFirst(baseUrl + "?page=1" + "&size=" + pageResult.getSize());
        if (pageResult.hasNext()) {
            links.setNext(baseUrl + "?page=" + (requestedPage + 1) + "&size=" + pageResult.getSize());
        }
        if (pageResult.hasPrevious()) {
            links.setPrev(baseUrl + "?page=" + (requestedPage - 1) + "&size=" + pageResult.getSize());
        }
        links.setLast(baseUrl + "?page=" + pageResult.getTotalPages() + "&size=" + pageResult.getSize());

        pagination.setLinks(links);
        response.setPagination(pagination);

        return response;
    }
}

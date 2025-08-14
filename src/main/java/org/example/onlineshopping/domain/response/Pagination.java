package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class Pagination<T> {
    private final String message;
    private final T data;
    private int currentPage;
    private Long totalItems;
    private int totalPages;
    private int pageSize;
}

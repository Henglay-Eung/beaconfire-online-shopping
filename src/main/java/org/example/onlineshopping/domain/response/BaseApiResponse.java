package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseApiResponse<T> {
    private final String message;
    private final T data;
}

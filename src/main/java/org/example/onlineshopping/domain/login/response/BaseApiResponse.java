package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseApiResponse<T> {
    private final String message;
    private final T data;
}

package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorBaseApiResponse {
    private final String error;
    private final int code;
}

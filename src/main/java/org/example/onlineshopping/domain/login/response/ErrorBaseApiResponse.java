package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorBaseApiResponse {
    private final String error;
    private final int code;
}

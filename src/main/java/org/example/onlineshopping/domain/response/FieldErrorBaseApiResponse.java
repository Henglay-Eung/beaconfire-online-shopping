package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FieldErrorBaseApiResponse {
    private final int code;
    private final List<Object> error;
}
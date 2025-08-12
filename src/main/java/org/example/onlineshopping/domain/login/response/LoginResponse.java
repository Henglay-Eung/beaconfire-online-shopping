package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public class LoginResponse {
    private String message;
    private String token;
}

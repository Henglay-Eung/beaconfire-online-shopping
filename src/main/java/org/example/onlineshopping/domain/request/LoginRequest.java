package org.example.onlineshopping.domain.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LoginRequest {
    @NotBlank(message = "username is required and must not be blank")
    private String username;
    @NotBlank(message = "password is required and must not be blank")
    private String password;
}

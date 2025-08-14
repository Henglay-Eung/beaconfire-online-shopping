package org.example.onlineshopping.domain.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserRequest {
    @NotBlank(message = "username is required and must not be blank")
    private final String username;
    @Email(message = "email is invalid")
    @NotBlank(message = "email is required and must not be blank")
    private final String email;
    @NotBlank(message = "email is required and must not be blank")
    private final String password;
}

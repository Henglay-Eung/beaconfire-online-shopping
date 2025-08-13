package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
public class LoginResponse implements Serializable {
    private String message;
    private String token;
}

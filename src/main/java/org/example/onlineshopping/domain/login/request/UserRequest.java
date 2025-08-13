package org.example.onlineshopping.domain.login.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.example.onlineshopping.entity.Permission;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Builder
public class UserRequest {
    @NonNull
    private final String username;
    @Email
    private final String email;
    @NonNull
    private final String password;
}

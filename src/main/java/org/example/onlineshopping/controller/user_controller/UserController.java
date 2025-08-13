package org.example.onlineshopping.controller.user_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.UserRequest;
import org.example.onlineshopping.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest);
        return new ResponseEntity<>("User has been successfully created.", HttpStatus.CREATED);
    }
}

package org.example.onlineshopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.LoginRequest;
import org.example.onlineshopping.domain.login.response.LoginResponse;
import org.example.onlineshopping.exception.InvalidCredentialsException;
import org.example.onlineshopping.security.AuthUserDetail;
import org.example.onlineshopping.security.JwtProvider;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (InvalidCredentialsException e) {
            throw new BadCredentialsException("Incorrect credentials, please try again.");
        }

        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal();
        String token = jwtProvider.createToken(authUserDetail);
        return ResponseEntity.ok(LoginResponse.builder()
                .message("Logged in successfully.")
                .token(token)
                .build());
    }

}

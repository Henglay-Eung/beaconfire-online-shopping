package org.example.onlineshopping.exception;

import io.jsonwebtoken.JwtException;
import org.example.onlineshopping.domain.login.response.ErrorBaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerAdvice {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorBaseApiResponse> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(new ErrorBaseApiResponse(e.getMessage(), HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorBaseApiResponse> handleNotFoundExceptions(NotFoundException e) {
        return new ResponseEntity<>(new ErrorBaseApiResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorBaseApiResponse> handleRuntimeExceptions(RuntimeException e) {
        return new ResponseEntity<>(new ErrorBaseApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}

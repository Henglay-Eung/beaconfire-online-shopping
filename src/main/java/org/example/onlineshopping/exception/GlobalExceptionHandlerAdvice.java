package org.example.onlineshopping.exception;

import org.example.onlineshopping.domain.response.ErrorBaseApiResponse;
import org.example.onlineshopping.domain.response.FieldErrorBaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandlerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorBaseApiResponse> handleNotFoundExceptions(NotFoundException e) {
        return new ResponseEntity<>(new ErrorBaseApiResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorBaseApiResponse> handleRuntimeExceptions(RuntimeException e) {
        return new ResponseEntity<>(new ErrorBaseApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldErrorBaseApiResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<Object> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            Map<String, String> objectError = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            objectError.put("field", fieldName);
            objectError.put("message", errorMessage);
            errors.add(objectError);
        });
        return new ResponseEntity<>(new FieldErrorBaseApiResponse(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
    }
}

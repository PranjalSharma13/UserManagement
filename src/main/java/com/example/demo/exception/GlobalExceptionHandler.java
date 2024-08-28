package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.modelmapper.spi.ErrorMessage;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BadCredentialsException.class) // when Invalid Credentials
    public ResponseEntity<ErrorMessage> handleInvalidCredentialsException(
            BadCredentialsException e) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(
            UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorMessage(e.getMessage()), HttpStatus.CONFLICT); // 409 Conflict
    }

}

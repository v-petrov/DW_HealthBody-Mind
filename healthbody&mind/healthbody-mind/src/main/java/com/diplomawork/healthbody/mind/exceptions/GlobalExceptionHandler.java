package com.diplomawork.healthbody.mind.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailValidation.class)
    public ResponseEntity<String> existingEmailHandler(EmailValidation e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationHandler(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationErrorHandler(MethodArgumentNotValidException e) {
        if (e.getBindingResult().hasErrors()) {
            String errorMessage = e.getBindingResult().getGlobalError() != null
                    ? e.getBindingResult().getGlobalError().getDefaultMessage()
                    : (e.getBindingResult().getFieldError() != null
                    ? e.getBindingResult().getFieldError().getDefaultMessage()
                    : "Validation error occurred.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error occurred.");
    }
}

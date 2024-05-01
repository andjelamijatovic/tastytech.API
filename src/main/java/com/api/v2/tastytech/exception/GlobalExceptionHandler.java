package com.api.v2.tastytech.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorDetails> handleException(Exception e) {
        MyErrorDetails errDetails = new MyErrorDetails(e.getMessage());
        return new ResponseEntity<>(errDetails, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        List<ObjectError> objErrors = ex.getBindingResult().getAllErrors();
        for(ObjectError err: objErrors) {
            String field = ((FieldError) err).getField();
            String msg = err.getDefaultMessage();
            errors.put(field, msg);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

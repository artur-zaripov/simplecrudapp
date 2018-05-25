package io.github.artur_zaripov.simplecrudapp.exception.handler;

import io.github.artur_zaripov.simplecrudapp.exception.RecordNotFoundException;
import io.github.artur_zaripov.simplecrudapp.model.ApiResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = RecordNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiResponse("Record not found", ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    protected ResponseEntity<Object> handleEmptyResult(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, new ApiResponse("Empty result", ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new ApiResponse("Validation failed", Arrays.toString(
                        ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate).toArray()
                )),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
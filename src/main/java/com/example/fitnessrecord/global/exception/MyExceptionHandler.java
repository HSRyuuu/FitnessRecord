package com.example.fitnessrecord.global.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    protected ErrorResponse handleMyException(MyException e){
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleValidException(ConstraintViolationException e){
        log.error("validation error 발생 !!!");
        List<String> errors = new ArrayList<>();

        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        while(iterator.hasNext()){
            ConstraintViolation<?> constraint = iterator.next();
            String field = constraint.getPropertyPath().toString();
            String message = constraint.getMessage();
            errors.add(field + ": " + message);
        }

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e){
        log.info("Exception.class handler");
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }


}

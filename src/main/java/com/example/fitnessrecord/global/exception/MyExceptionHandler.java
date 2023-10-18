package com.example.fitnessrecord.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    protected ErrorResponse handleMyException(MyException e){
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleException(Exception e){
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }


}

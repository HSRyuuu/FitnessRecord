package com.example.fitnessrecord.global.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;

    public ErrorResponse(ErrorCode errorCode){
        this.statusCode = errorCode.getStatusCode();;
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.statusCode = errorCode.getStatusCode();;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

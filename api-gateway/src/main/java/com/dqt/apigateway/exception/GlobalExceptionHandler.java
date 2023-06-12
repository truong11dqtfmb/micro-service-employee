package com.dqt.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(CustomAbstractErrorWebExceptionHandler.class)
//    public ResponseEntity<ApiResponse> handleResourceNotFoundException(CustomAbstractErrorWebExceptionHandler ex) {
//        String message = ex.getMessage();
//        ApiResponse apiResponse = new ApiResponse(false, message);
//        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
//    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleException(CustomException ex) {
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(false, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


}

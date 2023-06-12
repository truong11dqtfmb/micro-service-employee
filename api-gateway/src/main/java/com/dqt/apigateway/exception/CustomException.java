package com.dqt.apigateway.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomException(){
        super("This is custom Exception");
    }

    public CustomException(String missingAuthorizationHeader) {
        super(missingAuthorizationHeader);
    }
}

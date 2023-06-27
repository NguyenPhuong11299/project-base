package com.automation.exceptions;

public class BadRequestException extends com.github.dockerjava.api.exception.BadRequestException {
    public BadRequestException(String message){
        super(String.format("Bad Request: ", message));
    }
}

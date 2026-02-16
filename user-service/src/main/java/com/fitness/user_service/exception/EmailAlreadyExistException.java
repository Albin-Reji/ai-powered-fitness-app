package com.fitness.user_service.exception;

public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException(String message) {
        super("Email Already Exist :"+message);
    }
}

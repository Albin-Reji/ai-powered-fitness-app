package com.fitness.activity_service.exception;

public class InvalidActivityId extends RuntimeException{
    public InvalidActivityId(String message) {
        super(message);
    }
}

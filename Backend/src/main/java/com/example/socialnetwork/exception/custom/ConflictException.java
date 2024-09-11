package com.example.socialnetwork.exception.custom;

public class ConflictException extends ClientErrorException {

    public ConflictException(String message) {
        super(message);
    }
}

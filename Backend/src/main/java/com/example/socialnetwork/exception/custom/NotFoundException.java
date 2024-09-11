package com.example.socialnetwork.exception.custom;

public class NotFoundException extends ClientErrorException {

    public NotFoundException(String message) {
        super(message);
    }
}

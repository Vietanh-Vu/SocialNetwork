package com.example.socialnetwork.exception.custom;

public class NotAllowException extends ClientErrorException {
    public NotAllowException(String message) {
        super(message);
    }
}

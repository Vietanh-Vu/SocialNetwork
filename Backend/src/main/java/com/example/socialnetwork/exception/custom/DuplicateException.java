package com.example.socialnetwork.exception.custom;

public class DuplicateException extends ClientErrorException {

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException() {
        super("Duplicate exception");
    }
}

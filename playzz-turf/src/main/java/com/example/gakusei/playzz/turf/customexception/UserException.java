package com.example.gakusei.playzz.turf.customexception;

public class UserException extends RuntimeException{
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}

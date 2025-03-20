package com.pockEtentertainmentApp.exception;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String message) {
        super(message);
    }
}

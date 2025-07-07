package com.jobhunter.jobhunter_be.exception.custom;

public class UsernameExistedException extends Exception{
    public UsernameExistedException(String message) {
        super(message);
    }
}

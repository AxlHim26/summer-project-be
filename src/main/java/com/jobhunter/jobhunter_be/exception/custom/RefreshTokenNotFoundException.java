package com.jobhunter.jobhunter_be.exception.custom;

public class RefreshTokenNotFoundException extends Exception{
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}

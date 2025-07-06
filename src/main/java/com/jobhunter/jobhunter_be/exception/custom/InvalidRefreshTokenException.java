package com.jobhunter.jobhunter_be.exception.custom;

public class InvalidRefreshTokenException extends Exception{
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}

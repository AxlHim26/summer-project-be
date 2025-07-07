package com.jobhunter.jobhunter_be.exception.custom;

public class ExpiredRefreshTokenException extends Exception{
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}

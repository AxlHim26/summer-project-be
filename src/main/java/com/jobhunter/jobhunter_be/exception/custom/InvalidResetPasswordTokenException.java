package com.jobhunter.jobhunter_be.exception.custom;

public class InvalidResetPasswordTokenException extends Exception{
    public InvalidResetPasswordTokenException(String message) {
        super(message);
    }
}

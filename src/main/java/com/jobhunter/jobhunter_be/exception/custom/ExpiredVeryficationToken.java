package com.jobhunter.jobhunter_be.exception.custom;

public class ExpiredVeryficationToken extends Exception{
    public ExpiredVeryficationToken(String message) {
        super(message);
    }
}

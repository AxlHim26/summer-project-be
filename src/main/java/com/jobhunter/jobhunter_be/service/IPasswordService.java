package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.exception.custom.InvalidResetPasswordTokenException;

public interface IPasswordService {
    void requestPasswordReset(String email);

    void sendLinkResetPassword(String resetLink, String username);

    void resetPassword(String token, String newPassword) throws InvalidResetPasswordTokenException;
}
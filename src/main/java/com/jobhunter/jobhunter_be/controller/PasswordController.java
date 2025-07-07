package com.jobhunter.jobhunter_be.controller;


import com.jobhunter.jobhunter_be.dto.common.ApiResponseCus;
import com.jobhunter.jobhunter_be.dto.request.ResetPasswordRequest;
import com.jobhunter.jobhunter_be.exception.custom.InvalidResetPasswordTokenException;
import com.jobhunter.jobhunter_be.service.impl.PasswordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordServiceImpl passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponseCus<Void>> forgotPassword(@RequestParam String email) {
        passwordService.requestPasswordReset(email);

        return ResponseEntity.ok(
                ApiResponseCus.success("Reset link sent to email")
        );
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponseCus<Void>> resetPassword(@RequestBody ResetPasswordRequest request) throws InvalidResetPasswordTokenException {
        passwordService.resetPassword(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(
                ApiResponseCus.success("Password reset successfully")
        );
    }
}
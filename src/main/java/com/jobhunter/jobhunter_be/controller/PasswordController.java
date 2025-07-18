package com.jobhunter.jobhunter_be.controller;


import com.jobhunter.jobhunter_be.dto.common.RestResponse;
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
    public ResponseEntity<RestResponse<Void>> forgotPassword(@RequestParam String email) {
        passwordService.requestPasswordReset(email);

        return ResponseEntity.ok(
                RestResponse.success("Reset link sent to email")
        );
    }

    @PostMapping("/reset")
    public ResponseEntity<RestResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) throws InvalidResetPasswordTokenException {
        passwordService.resetPassword(request.getToken(), request.getNewPassword());

        return ResponseEntity.ok(
                RestResponse.success("Password reset successfully")
        );
    }
}
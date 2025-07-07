package com.jobhunter.jobhunter_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}

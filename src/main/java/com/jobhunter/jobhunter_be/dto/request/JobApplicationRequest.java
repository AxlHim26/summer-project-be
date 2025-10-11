package com.jobhunter.jobhunter_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationRequest {
    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;
    
    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;
    
    @Size(max = 500, message = "LinkedIn URL must not exceed 500 characters")
    private String linkedinUrl;
    
    @Size(max = 500, message = "Portfolio URL must not exceed 500 characters")
    private String portfolioUrl;
    
    @Size(max = 2000, message = "Additional information must not exceed 2000 characters")
    private String additionalInfo;
    
    @NotBlank(message = "Resume is required")
    private String resume; // Base64 encoded file or file path
}


package com.jobhunter.jobhunter_be.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {
    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;
    
    @NotEmpty(message = "Employment types are required")
    private List<String> employmentTypes;
    
    @NotNull(message = "Minimum salary is required")
    @Min(value = 0, message = "Minimum salary must be positive")
    private Integer salaryMin;
    
    @NotNull(message = "Maximum salary is required")
    @Min(value = 0, message = "Maximum salary must be positive")
    private Integer salaryMax;
    
    @NotEmpty(message = "Categories are required")
    private List<String> categories;
    
    @NotEmpty(message = "Required skills are required")
    private List<String> requiredSkills;
    
    @NotBlank(message = "Job description is required")
    @Size(max = 5000, message = "Job description must not exceed 5000 characters")
    private String jobDescription;
    
    @NotBlank(message = "Responsibilities are required")
    @Size(max = 5000, message = "Responsibilities must not exceed 5000 characters")
    private String responsibilities;
    
    @NotBlank(message = "Qualifications are required")
    @Size(max = 5000, message = "Qualifications must not exceed 5000 characters")
    private String qualifications;
    
    @Size(max = 5000, message = "Nice to haves must not exceed 5000 characters")
    private String niceToHaves;
}

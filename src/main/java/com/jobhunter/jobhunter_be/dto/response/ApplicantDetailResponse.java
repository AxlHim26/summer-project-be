package com.jobhunter.jobhunter_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDetailResponse {
    private String applicationId;
    private String candidateId;
    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String jobId;
    private String jobName;
    private String jobType;
    private String salary;
    private String category;
    private String status;
    private String appliedAt;
    private String resumeId;
    private String currentJob;
    private String portfolioLink;
    private String about;
    private String experience;
    private String education;
    private String skills;
    private String address;
    private String avatar;
}


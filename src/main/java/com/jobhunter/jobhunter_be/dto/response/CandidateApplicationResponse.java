package com.jobhunter.jobhunter_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateApplicationResponse {
    private String id;
    private String jobId;
    private String jobName;
    private String companyName;
    private String location;
    private String jobType;
    private String status;
    private String appliedAt;
    private String salary;
    private String category;
}


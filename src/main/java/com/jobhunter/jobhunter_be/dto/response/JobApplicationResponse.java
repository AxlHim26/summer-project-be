package com.jobhunter.jobhunter_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponse {
    private String id;
    private String jobId;
    private String jobName;
    private String candidateName;
    private String candidateEmail;
    private String status;
    private String appliedAt;
}

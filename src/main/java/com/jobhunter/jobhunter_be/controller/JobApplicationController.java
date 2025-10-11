package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.JobApplicationRequest;
import com.jobhunter.jobhunter_be.dto.response.JobApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.CandidateApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.RecruiterApplicantResponse;
import com.jobhunter.jobhunter_be.dto.response.ApplicantDetailResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.JobApplicationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationServiceImpl jobApplicationService;

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/{jobId}/apply")
    public ResponseEntity<RestResponse<JobApplicationResponse>> applyToJob(
            @PathVariable String jobId,
            @Valid @RequestBody JobApplicationRequest request,
            Authentication authentication
    ) throws NotFoundException {
        JobApplicationResponse response = jobApplicationService.applyToJob(jobId, request, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Application submitted successfully"));
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/applications")
    public ResponseEntity<RestResponse<List<CandidateApplicationResponse>>> getCandidateApplications(
            Authentication authentication
    ) throws NotFoundException {
        List<CandidateApplicationResponse> response = jobApplicationService.getCandidateApplications(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Applications fetched successfully"));
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/{jobId}/applied")
    public ResponseEntity<RestResponse<Boolean>> hasAppliedToJob(
            @PathVariable String jobId,
            Authentication authentication
    ) throws NotFoundException {
        boolean hasApplied = jobApplicationService.hasAppliedToJob(jobId, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(hasApplied, "Application status checked successfully"));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/applicants")
    public ResponseEntity<RestResponse<List<RecruiterApplicantResponse>>> getRecruiterApplicants(
            Authentication authentication
    ) throws NotFoundException {
        List<RecruiterApplicantResponse> response = jobApplicationService.getRecruiterApplicants(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Applicants fetched successfully"));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/recruiter/applicants/{applicationId}")
    public ResponseEntity<RestResponse<ApplicantDetailResponse>> getApplicantDetail(
            @PathVariable String applicationId,
            Authentication authentication
    ) throws NotFoundException {
        ApplicantDetailResponse response = jobApplicationService.getApplicantDetail(applicationId, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Applicant detail fetched successfully"));
    }
}

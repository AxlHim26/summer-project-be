package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.JobRequest;
import com.jobhunter.jobhunter_be.dto.response.JobResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.JobServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobServiceImpl jobService;

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<RestResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request, 
            Authentication authentication
    ) throws NotFoundException {
        JobResponse response = jobService.createJob(request, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Job created successfully"));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping
    public ResponseEntity<RestResponse<JobResponse[]>> getJobsByRecruiter(Authentication authentication) throws NotFoundException {
        JobResponse[] response = jobService.getJobsByRecruiter(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Jobs fetched successfully"));
    }

    // Public endpoint for candidates to view all available jobs
    @GetMapping("/public")
    public ResponseEntity<RestResponse<JobResponse[]>> getAllAvailableJobs() {
        JobResponse[] response = jobService.getAllAvailableJobs();
        return ResponseEntity.ok(RestResponse.success(response, "Available jobs fetched successfully"));
    }

    // Public endpoint for candidates to view job detail by ID
    @GetMapping("/public/{jobId}")
    public ResponseEntity<RestResponse<JobResponse>> getJobById(@PathVariable String jobId) throws NotFoundException {
        JobResponse response = jobService.getJobById(jobId);
        return ResponseEntity.ok(RestResponse.success(response, "Job detail fetched successfully"));
    }
}

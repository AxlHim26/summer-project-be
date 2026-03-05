package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.RecruiterRequest;
import com.jobhunter.jobhunter_be.dto.response.RecruiterResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.RecruiterServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterServiceImpl recruiterService;

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/setting")
    public ResponseEntity<RestResponse<RecruiterResponse>> getRecruiter(Authentication authentication) throws NotFoundException {
        RecruiterResponse response = recruiterService.getRecruiter(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Recruiter fetched successfully"));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/setting")
    public ResponseEntity<RestResponse<RecruiterResponse>> updateRecruiter(@Valid @RequestBody RecruiterRequest request, Authentication authentication) throws NotFoundException {
        RecruiterResponse response = recruiterService.updateRecruiter(request, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Recruiter information updated successfully"));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/profile")
    public ResponseEntity<RestResponse<RecruiterResponse>> getRecruiterProfile(Authentication authentication) throws NotFoundException {
        RecruiterResponse response = recruiterService.getRecruiter(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Recruiter profile fetched successfully"));
    }
}

package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.CandidateRequest;
import com.jobhunter.jobhunter_be.dto.response.CandidateResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.CandidateServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateServiceImpl candidateService;

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/setting")
    public ResponseEntity<RestResponse<CandidateResponse>> getCandidate(Authentication authentication) throws NotFoundException {
        CandidateResponse response = candidateService.getCandidate(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Candidate fetched successfully"));
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PutMapping("/setting")
    public ResponseEntity<RestResponse<CandidateResponse>> updateCandidate(@Valid @RequestBody CandidateRequest request, Authentication authentication) throws NotFoundException {
        CandidateResponse response = candidateService.updateCandidate(request, authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Candidate information updated successfully"));
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping("/profile")
    public ResponseEntity<RestResponse<CandidateResponse>> getCandidateProfile(Authentication authentication) throws NotFoundException {
        CandidateResponse response = candidateService.getCandidate(authentication.getName());
        return ResponseEntity.ok(RestResponse.success(response, "Candidate profile fetched successfully"));
    }
}

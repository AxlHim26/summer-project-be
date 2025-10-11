package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.CandidateRequest;
import com.jobhunter.jobhunter_be.dto.response.CandidateResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

public interface ICandidateService {
    CandidateResponse getCandidate(String email) throws NotFoundException;
    CandidateResponse updateCandidate(CandidateRequest request, String email) throws NotFoundException;
}

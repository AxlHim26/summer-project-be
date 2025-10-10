package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.RecruiterRequest;
import com.jobhunter.jobhunter_be.dto.response.RecruiterResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

public interface IRecruiterService {
    RecruiterResponse getRecruiter(String email) throws NotFoundException;

    RecruiterResponse updateRecruiter(RecruiterRequest request, String email) throws NotFoundException;

}

package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.JobApplicationRequest;
import com.jobhunter.jobhunter_be.dto.response.JobApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.CandidateApplicationResponse;
import com.jobhunter.jobhunter_be.dto.response.RecruiterApplicantResponse;
import com.jobhunter.jobhunter_be.dto.response.ApplicantDetailResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

import java.util.List;

public interface IJobApplicationService {
    JobApplicationResponse applyToJob(String jobId, JobApplicationRequest request, String candidateEmail) throws NotFoundException;
    List<CandidateApplicationResponse> getCandidateApplications(String candidateEmail) throws NotFoundException;
    boolean hasAppliedToJob(String jobId, String candidateEmail) throws NotFoundException;
    List<RecruiterApplicantResponse> getRecruiterApplicants(String recruiterEmail) throws NotFoundException;
    ApplicantDetailResponse getApplicantDetail(String applicationId, String recruiterEmail) throws NotFoundException;
}

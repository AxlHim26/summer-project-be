package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.JobRequest;
import com.jobhunter.jobhunter_be.dto.response.JobResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

public interface IJobService {
    JobResponse createJob(JobRequest request, String email) throws NotFoundException;
    JobResponse[] getJobsByRecruiter(String email) throws NotFoundException;
    JobResponse[] getAllAvailableJobs();
    JobResponse getJobById(String jobId) throws NotFoundException;
}


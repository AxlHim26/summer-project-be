package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Application;
import com.jobhunter.jobhunter_be.entity.Candidate;
import com.jobhunter.jobhunter_be.entity.Job;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByCandidateAndJob(Candidate candidate, Job job);
    List<Application> findByCandidateOrderByCreatedAtDesc(Candidate candidate);
    List<Application> findByJobRecruiterOrderByCreatedAtDesc(Recruiter recruiter);
}

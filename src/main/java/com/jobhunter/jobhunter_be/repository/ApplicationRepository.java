package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Application;
import com.jobhunter.jobhunter_be.entity.Candidate;
import com.jobhunter.jobhunter_be.entity.Job;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByCandidateAndJob(Candidate candidate, Job job);

    @EntityGraph(attributePaths = {
            "job",
            "job.recruiter",
            "job.recruiter.user",
            "candidate",
            "candidate.user",
            "candidate.user.profile",
            "resume",
            "stage"
    })
    List<Application> findByCandidateOrderByCreatedAtDesc(Candidate candidate);

    @EntityGraph(attributePaths = {
            "job",
            "job.recruiter",
            "job.recruiter.user",
            "candidate",
            "candidate.user",
            "candidate.user.profile",
            "resume",
            "stage"
    })
    List<Application> findByJobRecruiterOrderByCreatedAtDesc(Recruiter recruiter);

    @EntityGraph(attributePaths = {
            "job",
            "job.recruiter",
            "job.recruiter.user",
            "candidate",
            "candidate.user",
            "candidate.user.profile",
            "resume",
            "stage"
    })
    @Query("SELECT a FROM Application a WHERE a.id = :id")
    Optional<Application> findDetailById(@Param("id") Long id);
}

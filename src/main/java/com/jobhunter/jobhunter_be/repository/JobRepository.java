package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Job;
import com.jobhunter.jobhunter_be.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByRecruiterOrderByCreateAtDesc(Recruiter recruiter);

    List<Job> findByExperiedDateAfterOrderByCreateAtDesc(Date currentDate);
}

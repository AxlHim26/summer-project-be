package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {

}

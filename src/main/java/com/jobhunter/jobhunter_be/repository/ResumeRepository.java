package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
}


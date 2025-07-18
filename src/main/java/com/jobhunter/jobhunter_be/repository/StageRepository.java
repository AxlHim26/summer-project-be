package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
    boolean existsByProgress(String progress);
}

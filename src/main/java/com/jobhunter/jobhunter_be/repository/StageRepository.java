package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StageRepository extends JpaRepository<Stage, Integer> {
    Optional<Stage> findByProgress(String progress);
    boolean existsByProgress(String progress);
}
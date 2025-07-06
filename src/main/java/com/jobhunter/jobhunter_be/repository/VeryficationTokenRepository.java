package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.entity.VeryficationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeryficationTokenRepository extends JpaRepository<VeryficationToken, Long> {
    Optional<VeryficationToken> findByToken(String token);
    void deleteByUser(User user);
}

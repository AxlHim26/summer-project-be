package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.recruiter r " +
            "LEFT JOIN FETCH u.profile p " +
            "WHERE u.email = :email")
    Optional<User> findUserWithRecruiterAndProfile(@Param("email") String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Integer> {
    @Query("""
                SELECT u.profile.socialLink
                FROM User u
                WHERE u.email = :email
            """)
    Optional<SocialLink> findSocialLinkByUserEmail(@Param("email") String email);

}

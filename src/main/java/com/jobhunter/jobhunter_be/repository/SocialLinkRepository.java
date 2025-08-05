package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Integer> {

}

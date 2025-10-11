package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_candidate", nullable = false)
    private String nameCandidate;

    @Column(name = "email_candidate", nullable = false)
    private String emailCandidate;

    @Column(name = "current_job", nullable = false)
    private String currentJob;

    @Column(name = "portfolio_link", nullable = false)
    private String portfolioLink;

    @Column(name = "about", nullable = false, columnDefinition = "TEXT")
    private String about;

    @Column(name = "cv", columnDefinition = "TEXT")
    private String cv;
}

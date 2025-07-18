package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @OneToOne()
    @JoinColumn(name = "recruiter_role_id")
    private RecruiterRole recruiterRole;

    @Column(name = "status", nullable = false)
    private String status;
}

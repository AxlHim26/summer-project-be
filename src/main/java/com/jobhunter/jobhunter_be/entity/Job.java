package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(
        name = "job",
        indexes = {
                @Index(name = "idx_job_recruiter", columnList = "recruiter_id"),
                @Index(name = "idx_job_expired_date", columnList = "experied_date"),
                @Index(name = "idx_job_created_at", columnList = "create_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @Column(name = "job_name",  nullable = false)
    private String jobName;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(name = "experied_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date experiedDate;

    @Column(name = "create_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "require_skill", nullable = false)
    private String requireSkill;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "who_are_you", nullable = false, columnDefinition = "TEXT")
    private String whoAreYou;

    @Column(name = "reponsibility", nullable = false, columnDefinition = "TEXT")
    private String reponsibility;

    @Column(name = "nice_to_have", nullable = false, columnDefinition = "TEXT")
    private String niceToHave;

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
    }
}

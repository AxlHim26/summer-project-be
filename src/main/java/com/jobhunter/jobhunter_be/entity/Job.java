package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "recruiter_id")
    @ManyToOne
    private Recruiter recruiter;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "require_skill")
    private String requireSkill;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "category")
    private String category;

    @Column(name = "who_you_are", columnDefinition = "text")
    private String whoYouAre;

    @Column(name = "responsibility", columnDefinition = "text")
    private String responsibility;

    @Column(name = "nice_to_have", columnDefinition = "text")
    private Integer niceToHave;

}

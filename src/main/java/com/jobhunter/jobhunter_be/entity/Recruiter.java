package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location")
    private String location;

    @Column(name = "industry")
    private String industry;

    @Column(name = "founded_date")
    private String foundedDate;

    @Column(name = "tech_stack")
    private String tech_stack;

    @Column(name = "benefit")
    private String benefit;

    @OneToMany(mappedBy = "job")
    private List<Job> jobs;
}

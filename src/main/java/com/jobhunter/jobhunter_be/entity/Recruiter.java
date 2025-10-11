package com.jobhunter.jobhunter_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recruiter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {
    @Id
    private Integer id;

    @Column(name = "founded_date", columnDefinition = "DATE")
    private Date foundedDate;

    @Column(name = "employee")
    private String employee;

    @Column(name = "location")
    private String location;

    @Column(name = "industry")
    private String industry;

    @Column(name = "tech_stack")
    private String techStack;

    @Column(name = "website")
    private String website;

    @Column(name = "benefit")
    private String benefit;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "recruiter", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Job> jobs;
}

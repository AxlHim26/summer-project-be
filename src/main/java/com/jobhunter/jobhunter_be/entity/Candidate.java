package com.jobhunter.jobhunter_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {
    @Id
    private Integer id;

    @Column(name = "experience",  nullable = false)
    private String experience;

    @Column(name = "education",   nullable = false)
    private String education;

    @Column(name = "skill",   nullable = false)
    private String skill;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Portfolio> portfolios;
}

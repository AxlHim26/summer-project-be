package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "current_job")
    private String currentJob;

    @Column(name = "portfolio_link")
    private String portfolioLink;

    @Column(name = "about")
    private String about;

    @Column(name = "cv")
    private String cv;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;
}

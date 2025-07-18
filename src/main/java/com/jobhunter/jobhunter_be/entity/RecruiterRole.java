package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
}

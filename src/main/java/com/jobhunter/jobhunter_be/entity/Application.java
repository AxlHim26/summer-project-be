package com.jobhunter.jobhunter_be.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "application",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_application_candidate_job", columnNames = {"candidate_id", "job_id"})
        },
        indexes = {
                @Index(name = "idx_application_candidate", columnList = "candidate_id"),
                @Index(name = "idx_application_job", columnList = "job_id"),
                @Index(name = "idx_application_stage", columnList = "stage_id"),
                @Index(name = "idx_application_created_at", columnList = "created_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<NotesApplication>  notesApplications;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}

package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "social_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "facebook_link", columnDefinition = "TEXT")
    private String facebookLink;

    @Column(name = "twitter_link", columnDefinition = "TEXT")
    private String twitterLink;

    @Column(name = "linkedin_link", columnDefinition = "TEXT")
    private String linkedinLink;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}

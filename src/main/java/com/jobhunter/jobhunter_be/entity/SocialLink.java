package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "id")
    private Integer id;

    @Column(name = "facebook_link", columnDefinition = "text")
    private String facebook_link;

    @Column(name = "twitter_link", columnDefinition = "text")
    private String twitter_link;

    @Column(name = "linkedin_link", columnDefinition = "text")
    private String linkedin_link;
}

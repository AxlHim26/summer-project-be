package com.jobhunter.jobhunter_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterResponse {
    private String name;
    private String avatar;
    private String website;
    private List<String> location;
    private String employee;
    private String industry;
    private String foundedDate;
    private List<String> techStack;
    private String description;
    private String benefit;
    // Contact information from SocialLink
    private String twitter;
    private String facebook;
    private String linkedin;
    private String instagram;
    private String email;
}

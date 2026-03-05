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
public class CandidateResponse {
    private String name;
    private String email;
    private String bio;
    private String address;
    private String phone;
    private String avatar;
    private String experience;
    private String education;
    private List<String> skills;
}


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
public class JobResponse {
    private String id;
    private String jobName;
    private String jobType;
    private String description;
    private String salary;
    private String category;
    private String requireSkill;
    private String whoAreYou;
    private String reponsibility;
    private String niceToHave;
    private Integer capacity;
    private String createdAt;
    private String expiredDate;
    private String status;
}


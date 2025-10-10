package com.jobhunter.jobhunter_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterResponse {
    private String name;
    private String avatar;
    private List<String> location;
    private String employee;
    private String industry;
    private Date foundedDate;
    private List<String> techStack;
    private String description;
    private String benefit;
}

package com.jobhunter.jobhunter_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterRequest {
    private String avatar;
    private String name;
    private List<String> location;
    private String employee;
    private String industry;
    private Date foundedDate;
    private List<String> techStack;
    private String description;
    private String benefit;
}

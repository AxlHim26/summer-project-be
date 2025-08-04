package com.jobhunter.jobhunter_be.dto.response;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Integer senderId;
    private String content;
    private String fileUrl;
    private Date createAt;
}

package com.jobhunter.jobhunter_be.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationListResponse {
    private Long id;
    private String lastMsg;
    private Date lastMsgTime;
    private String partnerName;
    private String partnerAvatar;
}

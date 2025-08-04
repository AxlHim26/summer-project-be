package com.jobhunter.jobhunter_be.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private Long conversationId;
    private Long senderId;
    private String content;
    private String fileUrl;
}

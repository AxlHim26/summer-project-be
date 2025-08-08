package com.jobhunter.jobhunter_be.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private Long conversationId;
    private String senderEmail;
    private String content;
    private String fileUrl;
}

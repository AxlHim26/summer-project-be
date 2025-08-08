package com.jobhunter.jobhunter_be.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageWebSocketResponse {
    private Long id;
    private Long conversationId;
    private String senderName;
    private String senderEmail;
    private String content;
    private String fileUrl;
    private LocalDateTime createAt;
}

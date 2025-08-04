package com.jobhunter.jobhunter_be.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageWebSocketResponse {
    private Long id;
    private Long conversationId;
    private int senderId;
    private String senderName;
    private String content;
    private String fileUrl;
    private String createAt;
}

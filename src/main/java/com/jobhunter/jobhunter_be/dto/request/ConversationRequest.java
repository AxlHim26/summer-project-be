package com.jobhunter.jobhunter_be.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    private List<String> participantEmails;
}

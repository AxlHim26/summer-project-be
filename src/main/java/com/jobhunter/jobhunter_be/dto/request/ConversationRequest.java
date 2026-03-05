package com.jobhunter.jobhunter_be.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {
    @NotNull(message = "Participants are required")
    @Size(min = 2, max = 2, message = "Conversation must have exactly 2 participants")
    private List<@Email(message = "Participant email should be valid") String> participantEmails;
}

package com.jobhunter.jobhunter_be.dto.response;

import com.jobhunter.jobhunter_be.entity.Message;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDetailResponse {
    private Long id;
    private Date createAt;
    private List<UserResponse> participants;
    private List<Message> messages;
}

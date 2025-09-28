package com.jobhunter.jobhunter_be.dto.response;

import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.entity.User;
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
    private List<User> participants;
    private List<Message> messages;
}

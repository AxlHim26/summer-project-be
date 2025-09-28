package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.request.SendMessageRequest;
import com.jobhunter.jobhunter_be.dto.response.MessageWebSocketResponse;
import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageServiceImpl messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    public void sendMessage(@DestinationVariable Long conversationId, SendMessageRequest request) {
        Message saved = messageService.saveMessage(conversationId, request.getSenderEmail(), request.getContent(), request.getFileUrl());
        MessageWebSocketResponse response = MessageWebSocketResponse.builder()
                .id(saved.getId())
                .conversationId(saved.getConversation().getId())
                .senderName(saved.getUser().getName())
                .senderEmail(saved.getUser().getEmail())
                .content(saved.getContent())
                .fileUrl(saved.getFileUrl())
                .createAt(LocalDateTime.now())
                .build();
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }
}

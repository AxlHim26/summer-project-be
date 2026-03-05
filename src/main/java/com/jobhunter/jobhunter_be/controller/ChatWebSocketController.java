package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.request.SendMessageRequest;
import com.jobhunter.jobhunter_be.dto.response.MessageWebSocketResponse;
import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MessageServiceImpl messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    public void sendMessage(@DestinationVariable Long conversationId, SendMessageRequest request, Principal principal) {
        String senderEmail = principal != null ? principal.getName() : request.getSenderEmail();

        try {
            Message saved = messageService.saveMessage(conversationId, senderEmail, request.getContent(), request.getFileUrl());
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
        } catch (NotFoundException | RuntimeException ex) {
            log.warn("WebSocket message rejected for conversation {}: {}", conversationId, ex.getMessage());
        }
    }
}

package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.response.MessageResponse;
import com.jobhunter.jobhunter_be.service.impl.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageServiceImpl messageService;

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<RestResponse<List<MessageResponse>>> getMessagesByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(RestResponse.success(messageService.getMessagesByConversation(conversationId),"Get Messages By Conversation Successfully"));
    }
}

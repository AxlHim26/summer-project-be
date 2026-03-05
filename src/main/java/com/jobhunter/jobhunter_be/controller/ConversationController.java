package com.jobhunter.jobhunter_be.controller;

import com.jobhunter.jobhunter_be.dto.common.RestResponse;
import com.jobhunter.jobhunter_be.dto.request.ConversationRequest;
import com.jobhunter.jobhunter_be.dto.response.ConversationDetailResponse;
import com.jobhunter.jobhunter_be.dto.response.ConversationListResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.service.impl.ConversationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationServiceImpl conversationService;

    @Operation(
            summary = "Create a new conversation",
            description = "Creates a conversation between users. If a 1-on-1 conversation already exists, returns that conversation."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversation created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<RestResponse<ConversationDetailResponse>> createConversation(
            @Valid @RequestBody ConversationRequest request,
            Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.ok(RestResponse.success(
                conversationService.createConversation(request, authentication.getName()),
                "Create conversation successfully"
        ));
    }

    @Operation(
            summary = "Get all conversations by user ID",
            description = "Returns a list of conversations including the last message and partner information."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conversations fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<RestResponse<List<ConversationListResponse>>> getAllConversationsByUserEmail(
            Authentication authentication
    ) throws NotFoundException {
        String email = authentication.getName();
        return ResponseEntity.ok(RestResponse.success(
                conversationService.getAllConversationsByEmail(email),
                "Get conversation by userId successfully"
        ));
    }
}

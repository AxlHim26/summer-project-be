package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.request.ConversationRequest;
import com.jobhunter.jobhunter_be.dto.response.ConversationDetailResponse;
import com.jobhunter.jobhunter_be.dto.response.ConversationListResponse;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

import java.util.List;

public interface IConversationService {
    ConversationDetailResponse createConversation(ConversationRequest request) throws NotFoundException;
    ConversationDetailResponse getConversationById(Long id) throws NotFoundException;
    List<ConversationListResponse> getAllConversationsByEmail(String email) throws NotFoundException;
}

package com.jobhunter.jobhunter_be.service;

import com.jobhunter.jobhunter_be.dto.response.MessageResponse;
import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;

import java.util.List;

public interface IMessageService {
    List<MessageResponse> getMessagesByConversation(Long conversationId, int page, int size) throws NotFoundException;
    Message saveMessage(Long conversationId, String senderEmail, String content, String fileUrl);
    MessageResponse updateMessage(Long messageId, Long senderId, String newContent) throws NotFoundException;
    void deleteMessage(Long messageId, Long senderId) throws NotFoundException;
}

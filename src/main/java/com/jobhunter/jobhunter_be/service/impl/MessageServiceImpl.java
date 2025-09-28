package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.response.MessageResponse;
import com.jobhunter.jobhunter_be.entity.Conversation;
import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.ConversationRepository;
import com.jobhunter.jobhunter_be.repository.MessageRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public List<MessageResponse> getMessagesByConversation(Long conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        List<MessageResponse> messages = messageRepository.findByConversationId(conversationId, pageable)
                .getContent().stream()
                .map(msg -> MessageResponse.builder()
                        .id(msg.getId())
                        .content(msg.getContent())
                        .fileUrl(msg.getFileUrl())
                        .createAt(msg.getCreateAt())
                        .senderEmail(msg.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());

        Collections.reverse(messages);
        return messages;
    }
    @Override
    public Message saveMessage(Long conversationId, String senderEmail, String content, String fileUrl) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .user(sender)
                .content(content)
                .fileUrl(fileUrl)
                .build();

        return messageRepository.save(message);
    }

    @Override
    public MessageResponse updateMessage(Long messageId, Long senderId, String newContent) throws NotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        if (message.getUser().getId() != senderId.intValue()) {
            throw new RuntimeException("You are not allowed to edit this message");
        }

        message.setContent(newContent);
        Message updated = messageRepository.save(message);

        return MessageResponse.builder()
                .id(updated.getId())
                .senderEmail(updated.getUser().getEmail())
                .content(updated.getContent())
                .createAt(updated.getCreateAt())
                .build();
    }

    @Override
    public void deleteMessage(Long messageId, Long senderId) throws NotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        if (message.getUser().getId() != senderId.intValue()) {
            throw new RuntimeException("You are not allowed to edit this message");
        }

        messageRepository.delete(message);
    }
}

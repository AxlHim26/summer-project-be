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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public List<MessageResponse> getMessagesByConversation(Long conversationId) {
        List<Message> messages = messageRepository.findByConversationId(conversationId);

        return messages.stream().map(msg -> MessageResponse.builder()
                .id(msg.getId())
                .content(msg.getContent())
                .fileUrl(msg.getFileUrl())
                .createAt(msg.getCreateAt())
                .senderId(msg.getUser().getId())
                .build()
        ).toList();
    }
    @Override
    public Message saveMessage(Long conversationId, Long senderId, String content, String fileUrl) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User sender = userRepository.findById(senderId)
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
                .senderId(updated.getUser().getId())
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

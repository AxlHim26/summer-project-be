package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.ConversationRequest;
import com.jobhunter.jobhunter_be.dto.response.ConversationDetailResponse;
import com.jobhunter.jobhunter_be.dto.response.ConversationListResponse;
import com.jobhunter.jobhunter_be.entity.Conversation;
import com.jobhunter.jobhunter_be.entity.ConversationParticipant;
import com.jobhunter.jobhunter_be.entity.Message;
import com.jobhunter.jobhunter_be.entity.User;
import com.jobhunter.jobhunter_be.exception.custom.NotFoundException;
import com.jobhunter.jobhunter_be.repository.ConversationParticipantRepository;
import com.jobhunter.jobhunter_be.repository.ConversationRepository;
import com.jobhunter.jobhunter_be.repository.MessageRepository;
import com.jobhunter.jobhunter_be.repository.UserRepository;
import com.jobhunter.jobhunter_be.service.IConversationService;
import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements IConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    @SneakyThrows
    @Override
    public ConversationDetailResponse createConversation(ConversationRequest request) {
        List<String> participantEmails = request.getParticipantEmails();

        String userA = participantEmails.get(0);
        String userB = participantEmails.get(1);

        Optional<Long> conversationId = conversationRepository.findConversationIdBtwUsers(userA, userB);
        if (conversationId.isPresent()) {
            return getConversationById(conversationId.get());
        }

        Conversation conversation = conversationRepository.save(Conversation.builder().build());

        List<User> users = participantEmails.stream()
                .map(email -> {
                    try {
                        return getUser(email);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        List<ConversationParticipant> participantsEntity = users.stream()
                .map(user -> ConversationParticipant.builder()
                        .conversation(conversation)
                        .user(user)
                        .build())
                .toList();

        participantRepository.saveAll(participantsEntity);

        List<User> participants = users.stream()
                .map(this::toMinimalUser)
                .toList();

        return ConversationDetailResponse.builder()
                .id(conversation.getId())
                .createAt(conversation.getCreateAt())
                .participants(participants)
                .build();
    }


    @Override
    public ConversationDetailResponse getConversationById(Long id) throws NotFoundException {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found with id: " + id));

        List<ConversationParticipant> participants = participantRepository.findByConversation(conversation);
        List<User> userResponses = participants.stream()
                .map(cp -> toMinimalUser(cp.getUser()))
                .collect(Collectors.toList());
        List<Message> messages = messageRepository.findByConversation_Id(id);

        return ConversationDetailResponse.builder()
                .id(conversation.getId())
                .createAt(conversation.getCreateAt())
                .participants(userResponses)
                .messages(messages)
                .build();
    }

    @Override
    public List<ConversationListResponse> getAllConversationsByEmail(String email) throws NotFoundException {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("User not found with email: " + email);
        }

        Set<Long> conversationsId = conversationRepository.findAllByUserEmail(email);

        return conversationsId.stream()
                .map(conversationId -> mapToConversationListResponse(conversationId, email))
                .toList();
    }

    private ConversationListResponse mapToConversationListResponse(Long conversationId, String email) {
        Message lastMessage = messageRepository.findLastMsgByConversationId(conversationId);
        List<User> participants = participantRepository.findParticipantsByConversationId(conversationId);

        User partner = participants.stream()
                .filter(u -> !Objects.equals(u.getEmail(), email))
                .findFirst()
                .orElse(null);

        return ConversationListResponse.builder()
                .id(conversationId)
                .lastMsg(lastMessage != null ? lastMessage.getContent() : "")
                .lastMsgTime(lastMessage != null ? lastMessage.getCreateAt() : null)
                .partnerName(partner != null ? partner.getName() : "Unknown")
                .partnerAvatar(partner != null && partner.getProfile() != null
                        ? partner.getProfile().getAvatar()
                        : "https://res.cloudinary.com/dhsv9jnul/image/upload/v1753185959/avatar-default_gvywqr.webp")
                .build();
    }

    private User toMinimalUser(User user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
    private User getUser(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
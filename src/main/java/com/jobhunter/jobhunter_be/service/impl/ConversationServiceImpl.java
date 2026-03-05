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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    @Override
    public ConversationDetailResponse createConversation(ConversationRequest request, String requesterEmail) throws NotFoundException {
        List<String> participantEmails = request.getParticipantEmails();
        if (participantEmails == null || participantEmails.size() != 2) {
            throw new IllegalArgumentException("Conversation must have exactly 2 participants");
        }
        if (!participantEmails.contains(requesterEmail)) {
            throw new AccessDeniedException("You cannot create a conversation without yourself");
        }

        String userA = participantEmails.get(0);
        String userB = participantEmails.get(1);
        if (Objects.equals(userA, userB)) {
            throw new IllegalArgumentException("Participants must be different users");
        }

        Optional<Long> conversationId = conversationRepository.findConversationIdBtwUsers(userA, userB);
        if (conversationId.isPresent()) {
            return getConversationById(conversationId.get());
        }

        Conversation conversation = conversationRepository.save(Conversation.builder().build());

        List<User> users = new ArrayList<>(participantEmails.size());
        for (String email : participantEmails) {
            users.add(getUser(email));
        }

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
        if (conversationsId.isEmpty()) {
            return List.of();
        }

        Map<Long, Message> latestMessagesByConversationId = messageRepository
                .findLatestMessagesByConversationIds(conversationsId)
                .stream()
                .collect(Collectors.toMap(
                        message -> message.getConversation().getId(),
                        message -> message,
                        (first, second) -> first.getCreateAt().after(second.getCreateAt()) ? first : second
                ));

        Map<Long, User> partnerByConversationId = new HashMap<>();
        for (ConversationParticipant participant : participantRepository.findByConversationIdsWithUserAndProfile(conversationsId)) {
            User participantUser = participant.getUser();
            if (!Objects.equals(participantUser.getEmail(), email)) {
                partnerByConversationId.putIfAbsent(participant.getConversation().getId(), participantUser);
            }
        }

        return conversationsId.stream()
                .map(conversationId -> {
                    Message lastMessage = latestMessagesByConversationId.get(conversationId);
                    User partner = partnerByConversationId.get(conversationId);

                    return ConversationListResponse.builder()
                            .id(conversationId)
                            .lastMsg(lastMessage != null ? lastMessage.getContent() : "")
                            .lastMsgTime(lastMessage != null ? lastMessage.getCreateAt() : null)
                            .partnerName(partner != null ? partner.getName() : "Unknown")
                            .partnerAvatar(partner != null && partner.getProfile() != null
                                    ? partner.getProfile().getAvatar()
                                    : "https://res.cloudinary.com/dhsv9jnul/image/upload/v1753185959/avatar-default_gvywqr.webp")
                            .build();
                })
                .sorted(Comparator.comparing(
                        ConversationListResponse::getLastMsgTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
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

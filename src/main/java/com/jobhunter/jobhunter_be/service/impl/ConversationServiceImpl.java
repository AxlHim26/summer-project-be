package com.jobhunter.jobhunter_be.service.impl;

import com.jobhunter.jobhunter_be.dto.request.ConversationRequest;
import com.jobhunter.jobhunter_be.dto.response.ConversationDetailResponse;
import com.jobhunter.jobhunter_be.dto.response.ConversationListResponse;
import com.jobhunter.jobhunter_be.dto.response.UserResponse;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public ConversationDetailResponse createConversation(ConversationRequest request) throws NotFoundException {
        List<Long> participantIds = request.getParticipantIds();

        // Check tồn tại conversation 1-1
        if (participantIds.size() == 2) {
            Long userA = participantIds.get(0);
            Long userB = participantIds.get(1);

            // Tìm conversation có đúng 2 participant này
            List<Long> conversationIdsUserA = participantRepository.findConversationIdsByUserId(userA);
            List<Long> conversationIdsUserB = participantRepository.findConversationIdsByUserId(userB);

            // Lấy conversation chung của 2 người
            conversationIdsUserA.retainAll(conversationIdsUserB);

            if (!conversationIdsUserA.isEmpty()) {
                // Lấy conversation đầu tiên đã tồn tại
                Long existingConversationId = conversationIdsUserA.getFirst();
                return getConversationById(existingConversationId);
            }
        }

        // Nếu chưa tồn tại, tạo mới
        Conversation conversation = conversationRepository.save(Conversation.builder().build());
        List<UserResponse> participants = new ArrayList<>();

        for (Long userId : participantIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            ConversationParticipant cp = ConversationParticipant.builder()
                    .conversation(conversation)
                    .user(user)
                    .build();
            participantRepository.save(cp);

            participants.add(mapUserToResponse(user));
        }

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
        List<UserResponse> userResponses = participants.stream()
                .map(cp -> mapUserToResponse(cp.getUser()))
                .collect(Collectors.toList());
        List<Message> messages = messageRepository.findByConversationId(id);

        return ConversationDetailResponse.builder()
                .id(conversation.getId())
                .createAt(conversation.getCreateAt())
                .participants(userResponses)
                .messages(messages)
                .build();
    }

    @Override
    public List<ConversationListResponse> getAllConversationsByUserId(Long userId) throws NotFoundException {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        List<Conversation> conversations = conversationRepository.findAllByUserId(userId);
        return conversations.stream().map(conversation -> {
            Message lastMessage = messageRepository.findLastMsgByConversationId(conversation.getId());
            List<User> participants = participantRepository.findParticipantsByConversationId(conversation.getId());
            User partner = participants.stream()
                    .filter(u -> u.getId() != userId.intValue())
                    .findFirst()
                    .orElse(null);
            return ConversationListResponse.builder()
                    .id(conversation.getId())
                    .lastMsg(lastMessage.getContent())
                    .lastMsgTime(lastMessage.getCreateAt())
                    .partnerName(partner != null ? partner.getName() : "Unknown")
                    .partnerAvatar(partner != null && partner.getProfile() != null
                            ? partner.getProfile().getAvatar()
                            : "https://res.cloudinary.com/dhsv9jnul/image/upload/v1753185959/avatar-default_gvywqr.webp")
                    .build();
        }).collect(Collectors.toList());
    }

private UserResponse mapUserToResponse(User user) {
    return UserResponse.builder()
            .id(user.getId())
            .fullname(user.getName())
            .build();
}
}
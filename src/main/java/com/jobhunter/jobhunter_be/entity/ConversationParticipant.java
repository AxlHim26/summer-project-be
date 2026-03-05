package com.jobhunter.jobhunter_be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "conversation_participant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_conversation_participant", columnNames = {"user_id", "conversation_id"})
        },
        indexes = {
                @Index(name = "idx_conversation_participant_user", columnList = "user_id"),
                @Index(name = "idx_conversation_participant_conversation", columnList = "conversation_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @JsonBackReference
    private Conversation conversation;
}

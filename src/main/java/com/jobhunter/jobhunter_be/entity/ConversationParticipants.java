package com.jobhunter.jobhunter_be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

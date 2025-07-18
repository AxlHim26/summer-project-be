package com.jobhunter.jobhunter_be.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conversation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<Message> messages;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<ConversationParticipant> participants;

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
    }
}

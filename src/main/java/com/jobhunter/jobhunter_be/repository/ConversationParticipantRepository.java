package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Conversation;
import com.jobhunter.jobhunter_be.entity.ConversationParticipant;
import com.jobhunter.jobhunter_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {
    @Query("""
        SELECT p.user
        FROM ConversationParticipant p
        WHERE p.conversation.id = :conversationId
    """)
    List<User> findParticipantsByConversationId(@Param("conversationId") Long conversationId);
    List<ConversationParticipant> findByConversation(Conversation conversation);
}

package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
                SELECT DISTINCT c
                FROM Conversation c
                JOIN ConversationParticipant p ON p.conversation = c
                WHERE p.user.id = :userId
            """)
    List<Conversation> findAllByUserId(@Param("userId") Long userId);

}

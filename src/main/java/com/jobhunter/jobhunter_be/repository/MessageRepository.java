package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation_Id(Long conversationId);

    @Query(value = """
                SELECT m
                FROM Message m
                JOIN FETCH m.user
                WHERE m.conversation.id = :conversationId
            """,
            countQuery = """
                SELECT COUNT(m)
                FROM Message m
                WHERE m.conversation.id = :conversationId
            """)
    Page<Message> findByConversationIdWithUser(@Param("conversationId") Long conversationId, Pageable pageable);

    @Query("""
                SELECT m
                FROM Message m
                WHERE m.conversation.id IN :conversationIds
                  AND m.createAt = (
                    SELECT MAX(m2.createAt)
                    FROM Message m2
                    WHERE m2.conversation.id = m.conversation.id
                  )
            """)
    List<Message> findLatestMessagesByConversationIds(@Param("conversationIds") Set<Long> conversationIds);

    Message findTopByConversation_IdOrderByCreateAtDesc(Long conversationId);
}

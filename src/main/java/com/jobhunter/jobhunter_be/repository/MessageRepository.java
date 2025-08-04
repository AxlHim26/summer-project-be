package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationId(Long conversationId);
    @Query("""
        SELECT m
        FROM Message m
        WHERE m.conversation.id = :conversationId
        ORDER BY m.createAt DESC
        LIMIT 1
    """)
    Message findLastMsgByConversationId(@Param("conversationId") Long conversationId);
}

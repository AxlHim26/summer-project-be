package com.jobhunter.jobhunter_be.repository;

import com.jobhunter.jobhunter_be.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("""
             SELECT DISTINCT c.id
             FROM Conversation c
             JOIN ConversationParticipant p ON p.conversation = c
             WHERE p.user.email = :email
            """)
    Set<Long> findAllByUserEmail(@Param("email") String email);

    @Query("""
                SELECT c.id
                FROM Conversation c
                JOIN ConversationParticipant p1 ON p1.conversation = c AND p1.user.email IN (:emailA, :emailB)
                JOIN ConversationParticipant p2 ON p2.conversation = c AND p2.user.email IN (:emailA, :emailB)
                GROUP BY c.id
                HAVING COUNT(DISTINCT p1.user.email) = 2
            """)
    Optional<Long> findConversationIdBtwUsers(@Param("emailA") String emailA,
                                              @Param("emailB") String emailB);


}

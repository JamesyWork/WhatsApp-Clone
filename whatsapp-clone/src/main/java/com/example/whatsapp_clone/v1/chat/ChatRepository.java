package com.example.whatsapp_clone.v1.chat;

import com.example.whatsapp_clone.v1.chat.model.entities.Chat;
import com.example.whatsapp_clone.v1.chat.model.entities.ChatConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {
    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID)
    List<Chat> findChatsBySenderId(@Param("senderId") String senderId);

    @Query(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER)
    Optional<Chat> findChatsBySenderAndRecipient(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
}

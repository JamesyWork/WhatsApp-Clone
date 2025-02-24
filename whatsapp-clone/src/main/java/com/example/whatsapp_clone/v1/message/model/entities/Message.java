package com.example.whatsapp_clone.v1.message.model.entities;

import com.example.whatsapp_clone.common.BaseAuditingEntity;
import com.example.whatsapp_clone.v1.chat.model.entities.Chat;
import com.example.whatsapp_clone.v1.message.model.enums.MessageState;
import com.example.whatsapp_clone.v1.message.model.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
@NamedQueries(value = {
        @NamedQuery(
                name = MessageConstants.FIND_MESSAGES_BY_CHAT_ID,
                query = "SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.createdDate"
        ),
        @NamedQuery(
                name = MessageConstants.SET_MESSAGES_TO_SEEN_BY_CHAT,
                query = "UPDATE Message SET state = :newState WHERE chat.id = :chatId"
        ),
        @NamedQuery(
                name = MessageConstants.FIND_LAST_MESSAGE_BY_CHAT_ID,
                query = "SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY createdDate DESC LIMIT 1"
        )
})
public class Message extends BaseAuditingEntity {
    @Id
    @SequenceGenerator(name = "msg_seg", sequenceName = "msg_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg_seq")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageState state;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    private String mediaFilePath;

}

package com.example.whatsapp_clone.v1.chat.model.entities;

import com.example.whatsapp_clone.common.BaseAuditingEntity;
import com.example.whatsapp_clone.v1.message.model.entities.Message;
import com.example.whatsapp_clone.v1.message.model.enums.MessageState;
import com.example.whatsapp_clone.v1.message.model.enums.MessageType;
import com.example.whatsapp_clone.v1.user.model.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
@NamedQueries(value = {
        @NamedQuery(
                name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
                query = "SELECT c FROM Chat c WHERE c.sender.id = :senderId OR c.recipient.id = :senderId order by createdDate DESC"
        ),
        @NamedQuery(
                name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER,
                query = "SELECT c FROM Chat c WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId) OR (c.sender.id = :recipientId AND c.recipient.id = :senderId) order by createdDate DESC"
        )
})
public class Chat extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(String senderId) {
        if(recipient.getId().equals(senderId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        }
        return recipient.getFirstName() + " " + recipient.getLastName();
    }

    @Transient
    public String getTargetChatName(String senderId) {
        if(sender.getId().equals(senderId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        }
        return recipient.getFirstName() + " " + recipient.getLastName();
    }

    @Transient
    public Long getUnreadMessages(String senderId) {
        return this.messages
                .stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m -> MessageState.SENT == m.getState())
                .count();
    }

    @Transient
    public String getLastMessage() {
        if (messages != null && !messages.isEmpty()) {
            if (messages.get(0).getType() != MessageType.TEXT) {
                return "Attachment";
            }
            return messages.get(0).getContent();
        }
        return null; // No messages available
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if (messages != null && !messages.isEmpty()) {
            return messages.get(0).getCreatedDate();
        }
        return null;
    }
}

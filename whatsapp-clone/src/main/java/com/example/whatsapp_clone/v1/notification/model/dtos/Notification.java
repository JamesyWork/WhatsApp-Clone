package com.example.whatsapp_clone.v1.notification.model.dtos;

import com.example.whatsapp_clone.v1.message.model.enums.MessageType;
import com.example.whatsapp_clone.v1.notification.model.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private String chatId;
    private String content;
    private String senderId;
    private String receiverId;
    private String chatName;
    private MessageType messageType;
    private NotificationType type;
    private byte[] media;
}

package com.example.whatsapp_clone.v1.message;

import com.example.whatsapp_clone.v1.chat.ChatRepository;
import com.example.whatsapp_clone.v1.chat.model.entities.Chat;
import com.example.whatsapp_clone.v1.file.FileService;
import com.example.whatsapp_clone.v1.file.FileUtils;
import com.example.whatsapp_clone.v1.message.model.dtos.MessageRequest;
import com.example.whatsapp_clone.v1.message.model.dtos.MessageResponse;
import com.example.whatsapp_clone.v1.message.model.entities.Message;
import com.example.whatsapp_clone.v1.message.model.enums.MessageState;
import com.example.whatsapp_clone.v1.message.model.enums.MessageType;
import com.example.whatsapp_clone.v1.notification.model.dtos.Notification;
import com.example.whatsapp_clone.v1.notification.NotificationService;
import com.example.whatsapp_clone.v1.notification.model.enums.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;
    private final FileService fileService;
    private final NotificationService notificationService;

    @Transactional
    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);
        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getType())
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getChatName(messageRequest.getSenderId()))
                .build();

        notificationService.sendNotification(message.getReceiverId(), notification);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> findChatMessage(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessageToSeen(String chatId, Authentication auth){
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String recipientId = getRecipientId(chat, auth);
        Message message = messageRepository.findLastMessageByChatIdAndReceiverId(chatId);
        if(message.getReceiverId().equals(auth.getName())) {
            messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);
            Notification notification = Notification.builder()
                    .chatId(chat.getId())
                    .type(NotificationType.SEEN)
                    .senderId(getSenderId(chat, auth))
                    .receiverId(recipientId)
                    .build();
            notificationService.sendNotification(recipientId, notification);
        }
    }

    @Transactional
    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication auth) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new EntityNotFoundException("Chat not found"));
        final String senderId = getSenderId(chat, auth);
        final String recipientId = getRecipientId(chat, auth);

        final String filePath = fileService.saveFile(file, senderId);
        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setReceiverId(recipientId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);
        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .type(NotificationType.IMAGE)
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .receiverId(recipientId)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(recipientId, notification);
    }

    private String getSenderId(Chat chat, Authentication auth) {
        if(chat.getSender().getId().equals(auth.getName())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat, Authentication auth) {
        if(chat.getSender().getId().equals(auth.getName())) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}

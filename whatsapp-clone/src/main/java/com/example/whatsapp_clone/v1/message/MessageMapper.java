package com.example.whatsapp_clone.v1.message;

import com.example.whatsapp_clone.v1.file.FileUtils;
import com.example.whatsapp_clone.v1.message.model.dtos.MessageResponse;
import com.example.whatsapp_clone.v1.message.model.entities.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .type(message.getType())
                .state(message.getState())
                .createdAt(message.getCreatedDate())
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }
}
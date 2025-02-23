package com.example.whatsapp_clone.v1.chat;

import com.example.whatsapp_clone.common.StringResponse;
import com.example.whatsapp_clone.v1.chat.model.dtos.ChatResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(
            Authentication auth
    ) {
        return ResponseEntity.ok(chatService.getChatsByReceiverId(auth));
    }

    @PostMapping
    public ResponseEntity<StringResponse> createChat(
            @RequestParam(name = "sender-id") String senderId,
            @RequestParam(name = "receiver-id") String receiverId
    ) {
        final String chatId = chatService.createChat(senderId, receiverId);
        StringResponse response = StringResponse.builder().response(chatId).build();
        return ResponseEntity.ok(response);
    }
}

package com.megrez.dokibackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megrez.dokibackend.dto.WebSocketMessageDTO;
import com.megrez.dokibackend.mapper.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageMapper messageMapper;


    public WebSocketController(SimpMessagingTemplate messagingTemplate,
                               MessageMapper messageMapper) {
        this.messagingTemplate = messagingTemplate;
        this.messageMapper = messageMapper;
    }

    // 处理客户端发送的消息
    @MessageMapping("/sendMessage")
    public void sendMessage(String message) {
        try {
            WebSocketMessageDTO webSocketMessageDTO = objectMapper.readValue(message, WebSocketMessageDTO.class);
            messageMapper.markAllMessagesAsRead(webSocketMessageDTO.getConversationId(), webSocketMessageDTO.getUserId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 用于推送自定义消息到前端
    public void pushMessageToClients(String message) {
        messagingTemplate.convertAndSend("/topic/channel", message);
    }

    // 推送消息给特定用户
    public void pushMessageToUser(Integer userId, String message) {
        // 通过用户ID来定向消息
        String destination = "/queue/notifications/" + userId;
        messagingTemplate.convertAndSend(destination, message);
    }
}

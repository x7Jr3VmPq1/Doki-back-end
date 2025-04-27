package com.megrez.dokibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// websocket消息传输对象
public class WebSocketMessageDTO {
    private String type;    // 消息类型
    private String conversationId;  // 会话id
    private Integer userId; // 发送者id
}

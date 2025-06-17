package com.megrez.dokibackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 私聊时的会话列表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationVO {
    private String conversationId; // 会话id
    private String username; // 用户名
    private Integer userId; // 用户id
    private String avatarUrl; // 头像
    private String lastMessage; // 最后一条消息
    private LocalDateTime lastMessageSentAt; // 最后一条消息发送时间
}

package com.megrez.dokibackend.vo;

import com.megrez.dokibackend.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
    // 会话 ID
    private String conversationId;
    // 私聊对象用户名
    private String username;
    // 私聊对象ID
    private Integer userId;
    // 私聊对象头像
    private String avatarUrl;
    // 消息列表
    private List<Message> messages;
}

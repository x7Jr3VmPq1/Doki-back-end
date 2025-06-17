package com.megrez.dokibackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 对应数据库表中的会话表
public class Conversation {
    // 会话ID(一个UUID字符串)
    private String id;
    // 会话类型('private': 单聊; 'group': 群聊)
    private String conversationType;
    // 用户ID
    private Integer userId;
    // 用户最近一次删除的时间
    private LocalDateTime lastDeletedAt;
    // 用户加入时间
    private LocalDateTime userJoinedAt;
    // 会话创建时间
    private LocalDateTime createdAt;
    // 最后一条消息的内容
    private String lastMessageContent;
    // 最后一条消息的时间
    private LocalDateTime lastMessageAt;
}

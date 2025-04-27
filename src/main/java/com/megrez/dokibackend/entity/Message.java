package com.megrez.dokibackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer id; // 自动生成的主键

    private Integer senderId; // 发送者ID

    private String senderName; // 发送者名称

    private String senderAvatarUrl; // 发送者头像URL

    private String message; // 消息内容

    private String attachmentUrl; // 附件URL

    private LocalDateTime sentAt; // 发送时间

    private boolean isRead; // 是否已读

    private boolean isRevoked; // 是否已撤回

    private Integer replyToId; // 回复的消息ID

    @JsonIgnore
    private String conversationId; // 会话ID(一个UUID字符串)
}

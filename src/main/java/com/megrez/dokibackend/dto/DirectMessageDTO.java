package com.megrez.dokibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectMessageDTO {
    // 发送用户id
    private Integer userId;
    // 消息内容
    private String messageString;
    // 目标用户id
    private Integer targetUserId;
    // 会话id
    private String conversationId;
    // 图片的BASE64
    private String pictureBase64;
}

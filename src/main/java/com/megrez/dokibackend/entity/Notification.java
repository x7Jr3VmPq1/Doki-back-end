package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;


// 系统通知实体
@Data
public class Notification {
    // 通知ID
    private Integer id;
    // 用户ID
    private Integer userId;
    // 通知类型
    private String type;
    // 通知内容
    private String message;
    // 是否已读
    private Boolean isRead;
    // 通知创建时间
    private LocalDateTime createdAt;
    // 生产者ID
    private Integer operatorId;
    // 产生操作的视频ID
    private Integer videoId;
    // 产生操作的评论ID
    private Integer commentId;
}

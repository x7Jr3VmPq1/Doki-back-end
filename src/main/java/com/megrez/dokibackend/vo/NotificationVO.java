package com.megrez.dokibackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationVO {
    // 消费者ID
    private Integer userId;
    // 操作类型（点赞，评论，收藏，关注）
    private String type;
    // 生产者头像URL
    private String avatarUrl;
    // 生产者用户名
    private String operatorName;
    // 生产者ID
    private Integer operatorId;
    // 操作时间
    private LocalDateTime createdAt;
    // 通知的具体内容(如果有)
    private String message;
    // 发生操作的视频ID
    private Integer videoId;
    // 发生操作的视频标题
    private String videoTitle;
    // 发生操作的视频缩略图
    private String videoThumbnail;
    // 发生操作的评论ID
    private Integer commentId;
    // 发生操作的评论内容
    private String commentContent;
}

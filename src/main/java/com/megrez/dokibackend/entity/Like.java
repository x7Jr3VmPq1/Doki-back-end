package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;
// 点赞记录表
@Data
public class Like {
    // 点赞记录ID
    private Integer id;
    // 视频ID
    private Integer videoId;
    // 用户ID
    private Integer userId;
    // 创建时间
    private LocalDateTime createdAt;
}

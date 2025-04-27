package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 用户观看历史
@Data
public class UserWatchHistory {
    // 观看历史ID
    private Integer historyId;
    // 用户ID
    private Integer userId;
    // 视频ID
    private Integer videoId;
    // 观看时间
    private LocalDateTime watchedAt;
}

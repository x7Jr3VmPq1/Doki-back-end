package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 关注表
@Data
public class Follow {
    // 关注记录ID
    private Integer id;
    // 关注者ID
    private Integer followerId;
    // 被关注者ID
    private Integer followingId;
    // 关注时间
    private LocalDateTime createdAt;
}

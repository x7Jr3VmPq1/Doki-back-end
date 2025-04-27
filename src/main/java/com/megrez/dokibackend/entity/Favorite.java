package com.megrez.dokibackend.entity;

import lombok.Data;

// Favorite实体类，用于存储用户收藏的视频信息
@Data
public class Favorite {
    // 收藏记录id
    private String id;
    // 用户id
    private String userId;
    // 视频id
    private String videoId;
    // 收藏时间
    private String createdAt;
    // 更新时间
    private String updatedAt;
}

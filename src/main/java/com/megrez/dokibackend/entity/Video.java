package com.megrez.dokibackend.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

// 视频实体类
@Data
public class Video {
    // 视频ID
    private Integer id;
    // 上传者ID
    private Integer userId;
    // 用户名
    private String userName;
    // 用户头像URL
    private String avatarUrl;
    // 视频标题
    private String title;
    // 视频描述
    private String description;
    // 视频URL
    private String videoUrl;
    // 视频缩略图URL
    private String thumbnailUrl;
    // 视频时长
    private Integer duration;
    // 视频播放量
    private Integer views;
    // 视频上传日期
    private LocalDateTime createdAt;
    // 视频更新时间
    private LocalDateTime updatedAt;
    // 视频分类
    private String category;
    // 视频点赞数
    private Integer likeCount;
    // 视频评论数
    private Integer commentCount;
    // 视频收藏数
    private Integer favoriteCount;
};

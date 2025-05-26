package com.megrez.dokibackend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 返回给前端的视频信息
@Data
public class VideoVO {
    // 视频id
    private Integer id;
    // 上传用户id
    private Integer userId;
    // 用户名
    private String userName;
    // 用户头像链接
    private String avatarUrl;
    // 视频标题
    private String title;
    // 视频描述
    private String description;
    // 视频标签
    private List<String> tags;
    // 视频链接
    private String videoUrl;
    // 视频封面链接
    private String thumbnailUrl;
    // 视频时长
    private Integer duration;
    // 视频播放量
    private Integer views;
    // 视频创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    // 视频分类
    private String category;
    // 点赞数
    private Integer likeCount;
    // 评论数
    private Integer commentCount;
    // 收藏数
    private Integer favoriteCount;
    // 当前用户是否收藏
    private boolean isFavorited;
    // 当前用户是否点赞
    private boolean isLiked;
}

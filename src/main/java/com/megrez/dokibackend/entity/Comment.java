package com.megrez.dokibackend.entity;

import lombok.Data;

import java.time.LocalDateTime;
// 评论实体类
@Data
public class Comment {
    private Integer id; // 评论的唯一标识符，通常由数据库自动生成
    private Integer videoId; // 评论所属的视频ID，表示该评论是针对哪个视频的
    private Integer userId; // 发表评论的用户ID，表示该评论是由哪个用户发布的
    private String content; // 评论的具体内容，即用户发表的文字内容
    private Integer parentCommentId; // 父评论ID，用于表示该评论是回复哪条评论的，如果为null则表示该评论为顶级评论
    private Integer likeCount; // 该评论的点赞数，表示有多少用户对该评论点赞
    private Integer dislikeCount; // 该评论的点踩数，表示有多少用户对该评论点踩
    private LocalDateTime createdAt; // 评论的创建时间，表示该评论是在什么时间发表的
}

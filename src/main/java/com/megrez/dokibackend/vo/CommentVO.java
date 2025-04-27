package com.megrez.dokibackend.vo;

import com.megrez.dokibackend.dto.CommentsDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {
    // 评论id
    private Integer id;
    // 用户id
    private Integer userId;
    // 用户头像
    private String avatarUrl;
    // 用户名
    private String username;
    // 评论内容
    private String content;
    // 父评论id
    private Integer parentCommentId;
    // 点赞数
    private Integer likeCount;
    // 创建时间
    private LocalDateTime createdAt;
    // 二级评论集合
    private List<CommentVO> replies;
    // 是否点赞
    private boolean isLiked;
}

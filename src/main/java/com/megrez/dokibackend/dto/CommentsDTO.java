package com.megrez.dokibackend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CommentsDTO 类用于表示评论的数据传输对象（DTO）。
 * 该类包含了评论的基本信息以及可能的回复评论列表。
 */
@Data
public class CommentsDTO {
    /**
     * 评论的唯一标识符。
     */
    private Integer id;

    /**
     * 发表评论的用户的唯一标识符。
     */
    private Integer userId;

    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 发表评论的用户名。
     */
    private String username;

    /**
     * 评论的内容。
     */
    private String content;
    /**
     * 评论所属的视频的唯一标识符。
     * 如果评论属于某个视频，则此字段不为空。
     */
    private Integer videoId;

    /**
     * 父评论的唯一标识符。如果该评论是回复其他评论的，则此字段不为空。
     */
    private Integer parentCommentId;

    /**
     * 评论的点赞数。
     */
    private Integer likeCount;

    /**
     * 评论的创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 该评论的回复评论列表。如果该评论没有回复，则此列表为空。
     */
    private List<CommentsDTO> replies;
}

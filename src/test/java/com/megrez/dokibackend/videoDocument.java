package com.megrez.dokibackend;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.vo.VideoVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class videoDocument {
    private Integer id;
    private String title;
    private String description;
    private String category;
    private String avatarUrl;
    private String thumbnailUrl;
    private String videoUrl;
    private Integer userId;
    private String userName;
    private Integer duration;
    private Integer views;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数，接受一个Video对象和一个tags字符串数组
    public videoDocument(VideoVO video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.category = video.getCategory();
        this.avatarUrl = video.getAvatarUrl();
        this.thumbnailUrl = video.getThumbnailUrl();
        this.videoUrl = video.getVideoUrl();
        this.userId = video.getUserId();
        this.userName = video.getUserName();
        this.duration = video.getDuration();
        this.views = video.getViews();
        this.likeCount = video.getLikeCount();
        this.favoriteCount = video.getFavoriteCount();
        this.commentCount = video.getCommentCount();
        this.tags = video.getTags();
        this.createdAt = video.getCreatedAt();
    }

}

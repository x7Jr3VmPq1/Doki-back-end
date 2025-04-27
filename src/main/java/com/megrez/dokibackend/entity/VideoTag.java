package com.megrez.dokibackend.entity;

import lombok.Data;

// 视频标签
@Data
public class VideoTag {
    // 标签id
    private Integer id;
    // 视频id
    private Integer videoId;
    // 标签
    private String tag;
}

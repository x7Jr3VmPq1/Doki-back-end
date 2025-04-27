package com.megrez.dokibackend.dto;

import lombok.Data;

/**
 * VideoUploadInfoDTO 类用于封装从前端接收的视频上传表单数据。
 */
@Data
public class VideoUploadInfoDTO {
    // 视频标题
    private String videoTitle;
    // 视频描述
    private String videoDesc;
    // 视频分类
    private String category;
    // 视频标签数组
    private String[] tags;
}
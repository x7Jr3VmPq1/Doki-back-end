package com.megrez.dokibackend.common;

import java.awt.*;

public class FileServerURL {
    /**
     * 文件服务器URL，默认为localhost:8081
     */
    public static final String fileServerURL = "http://localhost:8081/";
    // 头像文件夹路径
    public static final String avatarFilesPath = fileServerURL + "avatars/";
    // 待审核发布视频路径
    public static final String pendingVideoFilesPath = fileServerURL + "pending_videos/";
    // 视频文件夹路径
    public static final String videoFilesPath = fileServerURL + "videos/";
    // 评论图片文件夹路径
    public static final String commentImageFilesPath = fileServerURL + "comment_imgs/";
}

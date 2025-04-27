package com.megrez.dokibackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 用户个人资料视图对象
public class UserProfilesVO {
    // 用户ID
    private Integer id;
    // 用户名
    private String userName;
    // 用户简介
    private String bio;
    // 用户头像
    private String avatarUrl;
    // 用户关注数
    private Integer followingCount;
    // 用户粉丝数
    private Integer followerCount;
    // 用户收到的点赞数
    private Integer likedCount;
    // 用户发布的视频
    private List<VideoVO> videos;
    // 用户点赞的视频
    private List<VideoVO> likedVideos;
    // 用户收藏的视频
    private List<VideoVO> favoriteVideos;
    // 当前用户是否关注了这个用户
    private Boolean isFollowing;
}

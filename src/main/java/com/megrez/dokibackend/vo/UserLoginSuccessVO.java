package com.megrez.dokibackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

// 登录成功返回的数据
@Data
@AllArgsConstructor
public class UserLoginSuccessVO {
    // token
    private String token;
    // 用户id
    private Integer userId;
    // 用户名
    private String userName;
    // 头像URL
    private String avatarUrl;
    // 个人简介
    private String bio;
    // 关注数
    private Integer followingCount;
    // 粉丝数
    private Integer followerCount;
}

package com.megrez.dokibackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 这个对象用来返回粉丝列表数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FansInfo {
    private Integer id; // 用户id
    private String userName; // 用户名
    private String avatarUrl; // 头像地址
    private String bio; // 签名
    private Integer followingCount; // 关注数
    private Integer followerCount; // 粉丝数
    private Integer likedCount; // 获赞数
    private Boolean isFollowing; // 是否已关注
}

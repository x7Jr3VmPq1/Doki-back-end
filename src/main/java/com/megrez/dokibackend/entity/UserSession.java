package com.megrez.dokibackend.entity;

import lombok.Data;

// 用户会话信息
@Data
public class UserSession {
    // 用户会话ID
    private Integer sessionId;
    // 用户ID
    private String userId;
    // 用户令牌
    private String token;
    // 登录时间
    private String loginTime;
    // 登出时间
    private String logoutTime;
}

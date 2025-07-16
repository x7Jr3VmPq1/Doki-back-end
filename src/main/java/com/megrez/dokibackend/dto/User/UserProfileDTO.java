package com.megrez.dokibackend.dto.User;

import lombok.*;

/**
 * 用户个人资料传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileDTO {
    // 用户名
    private String userName;
    // 个人简介
    private String bio;
    // 头像
    private String avatarBase64;
}

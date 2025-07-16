package com.megrez.dokibackend.dto.User;

import lombok.Data;

// 登陆时需要的信息
@Data
public class UserLoginDTO {
    // 用户邮箱
    private String email;
    // 用户密码
    private String password;
}

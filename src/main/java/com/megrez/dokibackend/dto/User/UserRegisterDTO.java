package com.megrez.dokibackend.dto.User;

import lombok.Data;

/**
 * 用户注册时传递的信息
 */
@Data
public class UserRegisterDTO {
    private String email;
    private String userName;
    private String password;
}

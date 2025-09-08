package com.megrez.dokibackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.megrez.dokibackend.entity.User;

public interface LoginAndRegisterService {

    // 短信验证码登录
    String loginBySms(String phone, String code) throws JsonProcessingException;

    // 密码登录
    String loginByPassword(String phone, String password);

    // 添加新用户
    User addNewUser(String phone);

    // 重置密码
    String resetPassword(String phone, String code, String newPassword);
}

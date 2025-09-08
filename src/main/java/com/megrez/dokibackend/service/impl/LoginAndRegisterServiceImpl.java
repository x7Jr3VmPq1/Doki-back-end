package com.megrez.dokibackend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.exception.InvalidCodeException;
import com.megrez.dokibackend.mapper.LoginAndRegisterMapper;
import com.megrez.dokibackend.mapper.UserMapper;
import com.megrez.dokibackend.service.LoginAndRegisterService;
import com.megrez.dokibackend.service.SmsService;
import com.megrez.dokibackend.utils.JWTUtil;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 用户登录/注册相关接口
 * 包括以下功能：
 * 1. 手机号注册
 * 2. 短信登录
 * 3. 密码登录
 * 4. 添加新用户
 * 5. 重置密码
 */
@Service
public class LoginAndRegisterServiceImpl implements LoginAndRegisterService {
    private final SmsService smsService;
    private final LoginAndRegisterMapper loginAndRegisterMapper;

    public LoginAndRegisterServiceImpl(SmsService smsService, LoginAndRegisterMapper loginAndRegisterMapper) {
        this.smsService = smsService;
        this.loginAndRegisterMapper = loginAndRegisterMapper;
    }

    @Override
    public String loginBySms(String phone, String code) throws JsonProcessingException {
        // 1. 核对手机号和验证码是否有效
        boolean result = smsService.verifyCode(phone, code);
        if (result) {
            // 2. 有效，判断用户是否已经注册，如果没有注册，添加新用户，返回token
            User userByPhone = loginAndRegisterMapper.getUserByPhone(phone);
            if (userByPhone == null) {
                User newUser = addNewUser(phone);
                return JWTUtil.generateToken(newUser.getUserName(), newUser.getId());
            } else {
                return JWTUtil.generateToken(userByPhone.getUserName(), userByPhone.getId());
            }
        } else {
            // 3. 无效，抛出验证码错误异常
            throw new InvalidCodeException();
        }
    }

    @Override
    public String loginByPassword(String phone, String password) {
        return "";
    }

    @Override
    public User addNewUser(String phone) {
        // 创建用户对象，给予默认值
        User newUser = new User();
        newUser.setUserName("用户" + new Random().nextInt(10000000));
        newUser.setPhoneNumber(phone);
        newUser.setAvatarUrl("http://localhost:8081/avatars/defaultAvatar.png");
        // 添加到表中
        loginAndRegisterMapper.addNewUser(newUser);
        // 把创建好的用户返回给调用处
        return newUser;
    }

    @Override
    public String resetPassword(String phone, String code, String newPassword) {
        return "";
    }
}

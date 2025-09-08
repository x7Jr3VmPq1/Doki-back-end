package com.megrez.dokibackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.exception.CodeNotConsumedException;
import com.megrez.dokibackend.exception.InvalidCodeException;
import com.megrez.dokibackend.service.LoginAndRegisterService;
import com.megrez.dokibackend.service.SmsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginAndRegisterController {
    private final LoginAndRegisterService loginAndRegisterService;
    private final SmsService smsService;

    public LoginAndRegisterController(LoginAndRegisterService loginAndRegisterService, SmsService smsService) {
        this.loginAndRegisterService = loginAndRegisterService;
        this.smsService = smsService;
    }

    /**
     * 手机号登录
     *
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/loginByPhone")
    Result<String> loginByPhone(String phone, String code) {
        try {
            return Result.success(loginAndRegisterService.loginBySms(phone, code));
        } catch (JsonProcessingException e) {
            return Result.error("未知错误");
        } catch (InvalidCodeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/getSmsCode")
    Result<String> getSmsCode(String phone) {
        try {
            return Result.success(smsService.sendCode(phone));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (CodeNotConsumedException e) {
            return Result.error(e.getMessage());
        }
    }
}

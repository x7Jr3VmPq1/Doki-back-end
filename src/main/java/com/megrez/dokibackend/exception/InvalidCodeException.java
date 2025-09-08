package com.megrez.dokibackend.exception;

public class InvalidCodeException extends SmsCodeException{
    public InvalidCodeException() {
        super("验证码错误");
    }
}

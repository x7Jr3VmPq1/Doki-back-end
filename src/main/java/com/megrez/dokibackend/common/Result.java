package com.megrez.dokibackend.common;

import lombok.Data;

import java.io.Serializable;

@Data

public class Result<T> implements Serializable {

    private Integer code; //编码：
    private String msg; //错误信息
    private T data; //数据

    private Result() {
    }

    // 进行增删改操作返回此接口
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = ResponseCode.SUCCESS;
        return result;
    }

    // 进行查询操作返回此接口
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = ResponseCode.SUCCESS;
        return result;
    }

    // 失败返回此接口
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.code = ResponseCode.INTERNAL_SERVER_ERROR;
        return result;
    }
}
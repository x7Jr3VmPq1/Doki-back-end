package com.megrez.dokibackend.utils;

import java.sql.SQLException;

public class SqlExceptionUtils {
    // 判断是否是唯一约束违反异常
    public static boolean isUniqueConstraintViolation(SQLException e) {
        // MySQL 的错误码 1062 表示唯一约束违反
        return e.getErrorCode() == 1062;
    }

    // 判断是否是外键约束违反
    public static boolean isForeignKeyConstraintViolation(SQLException e) {
        // MySQL 错误码 1452 表示外键约束违反
        return e.getErrorCode() == 1452;
    }
}

package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface LoginAndRegisterMapper {
    /**
     * 根据手机号获取用户信息
     *
     * @param phone
     * @return
     */
    @Select("SELECT * FROM users WHERE phone_number = #{phone}")
    User getUserByPhone(String phone);

    /**
     * 添加新用户
     *
     * @return
     */
    @Insert("INSERT INTO users (username,phone_number,avatar_url)" +
            " VALUE (#{userName},#{phoneNumber},#{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addNewUser(User newUser);
}

package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.vo.UserProfilesVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 新增用户
     *
     * @param userName     用户名
     * @param passwordHash 密码哈希值
     * @param email        邮箱
     */
    void addUser(
            @Param("userName") String userName,
            @Param("passwordHash") String passwordHash,
            @Param("avatarUrl") String avatarUrl,
            @Param("email") String email,
            @Param("bio") String bio
    );

    /**
     * 通过邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getUserByUserEmail(@Param("email") String email);

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(@Param("id") Integer id);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUserName(@Param("username") String username);

    /**
     * 根据用户ID修改用户信息
     *
     * @param id           用户ID
     * @param username     用户名
     * @param passwordHash 密码哈希值
     * @param email        邮箱
     * @param avatarUrl    头像URL
     * @param phoneNumber  手机号
     * @param bio          个人简介
     */
    void updateUser(
            @Param("id") Integer id,
            @Param("username") String username,
            @Param("passwordHash") String passwordHash,
            @Param("email") String email,
            @Param("avatarUrl") String avatarUrl,
            @Param("phoneNumber") String phoneNumber,
            @Param("bio") String bio);

    /**
     * 查询用户是否关注了某个用户
     *
     * @param userId
     * @param followingId
     * @return
     */
    Boolean isFollowing(@Param("followerId") Integer userId, @Param("followingId") Integer followingId);

    /**
     * 添加关注
     *
     * @param userId
     * @param followingId
     */
    void addFollow(@Param("followerId") Integer userId, @Param("followingId") Integer followingId);

    /**
     * 取消关注
     *
     * @param userId
     * @param followingId
     */
    void delFollow(@Param("followerId") Integer userId, @Param("followingId") Integer followingId);

    /**
     * 根据用户名查询用户存在情况
     *
     * @param username
     * @return
     */
    Integer countByUsername(@Param("username") String username, @Param("userId") Integer userId);

    /**
     * 根据用户名搜索用户
     *
     * @param userName
     * @return
     */
    List<User> searchUsers(@Param("userName") String userName);

    /**
     * 获取用户关注列表
     *
     * @param operatorId 操作者ID
     * @param userId     用户ID
     * @param size       每页大小
     * @param lastUserId 上次查询时列表中最后一个用户的ID(不必须)
     */
    List<UserProfilesVO> getFollowingList(
            @Param("operatorId") Integer operatorId,
            @Param("userId") Integer userId,
            @Param("size") Integer size,
            @Param("lastUserId") Integer lastUserId);

    /**
     * 获取用户粉丝列表
     *
     * @param operatorId 操作者ID
     * @param userId     查询目标用户ID
     * @param size       每页大小
     * @param lastUserId 上次查询时列表中最后一个用户的ID(不必须)
     */
    List<UserProfilesVO> getFansList(
            @Param("operatorId") Integer operatorId,
            @Param("userId") Integer userId,
            @Param("size") Integer size,
            @Param("lastUserId") Integer lastUserId);
}

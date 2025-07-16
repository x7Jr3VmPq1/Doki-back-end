package com.megrez.dokibackend.service;

import com.megrez.dokibackend.dto.User.UserLoginDTO;
import com.megrez.dokibackend.dto.User.UserRegisterDTO;
import com.megrez.dokibackend.vo.UserLoginSuccessVO;
import com.megrez.dokibackend.vo.UserProfilesVO;
import com.megrez.dokibackend.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 添加新用户。
     *
     * @param user 用户注册信息数据传输对象，包含用户注册所需的基本信息。
     */
    void addUser(UserRegisterDTO user);

    /**
     * 用户登录验证。
     *
     * @param userLoginDTO 用户登录信息数据传输对象，包含用户登录所需的凭证信息。
     * @return 用户登录成功数据传输对象，包含登录成功后的用户信息和令牌等。
     */
    UserLoginSuccessVO userLogin(UserLoginDTO userLoginDTO);


    /**
     * 根据用户ID获取用户信息。
     *
     * @param userId 用户ID
     * @return 用户登录成功数据传输对象。
     */
    UserLoginSuccessVO getUserInfoByToken(Integer userId);

    /**
     * 更新用户个人资料。
     *
     * @param userId       用户ID
     * @param userName     新用户名
     * @param bio          新个人简介
     * @param avatarBase64 新头像的Base64对象
     * @return 更新后的头像URL
     */
    UserProfilesVO updateUserProfile(Integer userId, String userName, String bio, String avatarBase64);

    /**
     * 获取用户个人资料。
     *
     * @param userId
     * @param userName
     * @return
     */
    UserProfilesVO getUserProfiles(Integer userId, String userName);

    /**
     * 关注/取消关注用户。
     *
     * @param userId
     * @param followingId
     * @return true 表示关注成功，false 表示取消关注成功。
     */
    Boolean followUser(Integer userId, Integer followingId);

    /**
     * 判断用户是否存在。
     *
     * @param userId         操作者ID
     * @param targetUserName 目标用户名
     * @return
     */
    Boolean isUserExist(Integer userId, String targetUserName);

    /**
     * 根据用户名搜索用户。
     *
     * @param userName 用户名
     * @return 用户列表
     */
    List<User> searchUsers(String userName);

    /**
     * 获取用户关注列表。
     *
     * @param operatorId 操作者ID
     * @param userId     查询目标用户ID
     * @param size       大小
     * @param lastUserId 上次加载列表最后一项的用户ID
     */
    List<UserProfilesVO> getFollowingList(Integer operatorId, Integer userId, Integer size, Integer lastUserId);

    /**
     * 获取用户粉丝列表。
     *
     * @param operatorId 操作者ID
     * @param userId     查询目标用户ID
     * @param size       大小
     * @param lastUserId 上次加载列表最后一项的用户ID
     */
    List<UserProfilesVO> getFansList(Integer operatorId, Integer userId, Integer size, Integer lastUserId);
}

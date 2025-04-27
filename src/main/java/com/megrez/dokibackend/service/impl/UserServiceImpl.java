package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.dto.UserLoginDTO;
import com.megrez.dokibackend.dto.UserRegisterDTO;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.exception.InvalidUserIdException;
import com.megrez.dokibackend.exception.UsernameAlreadyExistsException;
import com.megrez.dokibackend.mapper.UserMapper;
import com.megrez.dokibackend.service.UserService;
import com.megrez.dokibackend.service.VideosService;
import com.megrez.dokibackend.utils.JWTUtil;
import com.megrez.dokibackend.utils.PasswordUtils;
import com.megrez.dokibackend.vo.UserLoginSuccessVO;
import com.megrez.dokibackend.vo.UserProfilesVO;
import com.megrez.dokibackend.vo.VideoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final VideosService videosService;
    // 上传头像路径
    @Value("${avatarUploadPath.path}")
    String avatarUploadPath;

    public UserServiceImpl(UserMapper userMapper, VideosService videosService) {
        this.userMapper = userMapper;
        this.videosService = videosService;
    }

    // 注册用户
    @Override
    public void addUser(UserRegisterDTO user) {
        User registerUser = new User();
        registerUser.setUserName(user.getUserName());
        registerUser.setEmail(user.getEmail());
        // 设置默认头像路径
        registerUser.setAvatarUrl("http://localhost:8081/avatars/defaultAvatar.png");
        // 设置默认简介
        registerUser.setBio("");
        // 检查用户名或邮箱是否已存在
        if (userMapper.getUserByUserEmail(user.getEmail()) != null || userMapper.getUserByUserName(user.getUserName()) != null) {
            throw new RuntimeException("用户名或邮箱已存在");
        }
        registerUser.setPasswordHash(PasswordUtils.hashPassword(user.getPassword()));
        userMapper.addUser(
                registerUser.getUserName(),
                registerUser.getPasswordHash(),
                registerUser.getAvatarUrl(),
                registerUser.getEmail(),
                registerUser.getBio());
    }

    // 登录用户
    @Override
    public UserLoginSuccessVO userLogin(UserLoginDTO userLoginDTO) {
        User user1 = userMapper.getUserByUserEmail(userLoginDTO.getEmail());
        if (user1 != null && PasswordUtils.matchPassword(userLoginDTO.getPassword(), user1.getPasswordHash())) {
            // 生成并返回 JWT Token
            String token = JWTUtil.generateToken(user1.getUserName(), user1.getId());
            return new UserLoginSuccessVO(token, user1.getId(), user1.getUserName(), user1.getAvatarUrl(), user1.getBio());
        } else {
            // 抛出登录失败的异常
            throw new RuntimeException("登录失败，用户名或密码错误");
        }
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userId   // 当前操作的用户ID
     * @param userName // 目标用户名
     * @return
     */
    @Override
    public UserProfilesVO getUserProfiles(Integer userId, String userName) {
        User user = userMapper.getUserByUserName(userName);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 隐藏密码
        user.setPasswordHash(null);
        // 创建 UserProfilesVO 对象，并复制属性
        UserProfilesVO userProfilesVO = new UserProfilesVO();
        BeanUtils.copyProperties(user, userProfilesVO);
        // 判断查询到的user结果是不是自己，如果是自己就不用设置是否已经关注了
        if (!userProfilesVO.getId().equals(userId)) {
            userProfilesVO.setIsFollowing(userMapper.isFollowing(userId, userProfilesVO.getId()));
        }
        // 获取目标用户的发布视频列表
        List<VideoVO> videoVOList = videosService.getVideosByUserName(userId, userName);
        // 获取目标用户的点赞视频列表
        List<VideoVO> likedVideos = videosService.getLikeVideosByUserName(userId, userName);
        // 获取目标用户的收藏视频列表
        List<VideoVO> favoriteVideos = videosService.getFavoriteVideosByUserName(userId, userName);

        userProfilesVO.setVideos(videoVOList);
        userProfilesVO.setLikedVideos(likedVideos);
        userProfilesVO.setFavoriteVideos(favoriteVideos);
        return userProfilesVO;
    }

    /**
     * 关注/取消关注用户
     *
     * @param userId      // 当前操作的用户ID
     * @param followingId // 目标用户ID
     */
    @Override
    public Boolean followUser(Integer userId, Integer followingId) {
        // 判断userId是否为空，为空直接抛出错误
        if (userId == null || followingId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        if (userMapper.isFollowing(userId, followingId)) {
            // 已经关注，取消关注
            userMapper.delFollow(userId, followingId);
            return false;
        } else {
            // 未关注，添加关注
            userMapper.addFollow(userId, followingId);
            return true;
        }
    }

    /**
     * 查询用户是否存在
     *
     * @param userId         用户ID
     * @param targetUserName 目标用户名
     * @return
     */

    @Override
    public Boolean isUserExist(Integer userId, String targetUserName) {
        // 如果返回1，表示用户存在，则返回true，否则返回false
        return userMapper.countByUsername(targetUserName, userId) > 0;
    }

    /**
     * 根据用户名搜索用户
     *
     * @param userName 用户名
     * @return
     */
    @Override
    public List<User> searchUsers(String userName) {
        return userMapper.searchUsers(userName);
    }

    /**
     * 获取关注列表
     *
     * @param userId     用户ID
     * @param size       大小
     * @param lastUserId 上次加载最后的用户ID
     */
    @Override
    public List<UserProfilesVO> getFollowingList(Integer operatorId, Integer userId, Integer size, Integer lastUserId) {
        return userMapper.getFollowingList(operatorId, userId, size, lastUserId);
    }

    /**
     * 获取粉丝列表
     *
     * @param operatorId 操作者ID
     * @param userId     查询目标用户ID
     * @param size       大小
     * @param lastUserId 上次加载列表最后一项的用户ID
     */
    @Override
    public List<UserProfilesVO> getFansList(Integer operatorId, Integer userId, Integer size, Integer lastUserId) {
        return userMapper.getFansList(operatorId, userId, size, lastUserId);
    }


    /**
     * 更新用户资料
     *
     * @param userId       用户ID
     * @param userName     新用户名
     * @param bio          新个人简介
     * @param avatarBase64 新头像的Base64对象
     * @return
     */
    @Override
    public UserProfilesVO updateUserProfile(Integer userId, String userName, String bio, String avatarBase64) {
        // 检查用户是否存在
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new InvalidUserIdException("用户不存在");
        }
        // 更新用户名和个人简介
        if (StringUtils.hasText(userName)) {
            user.setUserName(userName);
        }
        // 检查 bio 是否为空，为空则设置为空字符串
        if (!StringUtils.hasText(bio)) {
            user.setBio("");
        } else {
            user.setBio(bio);
        }

        // 处理头像（Base64转为文件并保存）
        if (StringUtils.hasText(avatarBase64)) {
            try {
                // 解析 Base64 字符串，去掉头部的 `data:image/jpeg;base64,` 部分
                String base64Data = avatarBase64.split(",")[1];

                // 将 Base64 字符串解码为字节数组
                byte[] avatarBytes = Base64Utils.decodeFromString(base64Data);

                // 生成唯一的文件名
                String fileName = UUID.randomUUID().toString() + ".jpg";

                // 确保目录存在
                Path directory = Paths.get(avatarUploadPath);
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                // 删除用户之前存储的头像
                if (user.getAvatarUrl() != null) {
                    Path oldFilePath =
                            Paths.get(avatarUploadPath).
                                    resolve(user.getAvatarUrl().
                                            substring(user.getAvatarUrl().lastIndexOf("/") + 1));
                    Files.deleteIfExists(oldFilePath);
                }
                // 保存头像到本地文件系统
                Path filePath = directory.resolve(fileName);
                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    fos.write(avatarBytes);
                }

                // 设置用户头像的 URL
                String avatarUrl = FileServerURL.avatarFilesPath + fileName;
                user.setAvatarUrl(avatarUrl);

            } catch (IOException e) {
                throw new RuntimeException("头像保存失败", e);
            }
        }

        // 更新用户信息到数据库
        try {
            userMapper.updateUser(
                    user.getId(),
                    user.getUserName(),
                    null,
                    null,
                    user.getAvatarUrl(),
                    null,
                    user.getBio());
            // 清除用户敏感信息
            user.setPasswordHash(null);
            user.setEmail(null);
            user.setPhoneNumber(null);

            // 转换为 UserProfilesVO 对象并返回
            UserProfilesVO userProfilesVO = new UserProfilesVO();
            BeanUtils.copyProperties(user, userProfilesVO);

            return userProfilesVO;
        } catch (DuplicateKeyException e) {
            throw new UsernameAlreadyExistsException("用户名已经存在");
        }
    }
}

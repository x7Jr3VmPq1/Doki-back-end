package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.dto.UserLoginDTO;
import com.megrez.dokibackend.dto.UserProfileDTO;
import com.megrez.dokibackend.dto.UserRegisterDTO;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.exception.InvalidUserIdException;
import com.megrez.dokibackend.exception.UsernameAlreadyExistsException;
import com.megrez.dokibackend.service.UserService;
import com.megrez.dokibackend.vo.UserLoginSuccessVO;
import com.megrez.dokibackend.vo.UserProfilesVO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<String> addUser(@RequestBody UserRegisterDTO user) {
        log.info("register user: {}", user);
        try {
            userService.addUser(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return Result.error("注册失败:" + e.getMessage());
        }
    }

    /**
     * 登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginSuccessVO> userLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("login user: {}", userLoginDTO);
        try {
            UserLoginSuccessVO userInfo = userService.userLogin(userLoginDTO);
            log.info(userLoginDTO.getEmail() + "登录成功");
            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error("登录失败");
        }
    }

    @GetMapping("/userInfoByToken")
    public Result<UserLoginSuccessVO> getUserInfoByToken(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("getUserInfoByToken: userId={}", userId);
        try {
            // 解析出token

            return Result.success(userService.getUserInfoByToken(userId));
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 获取用户信息
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/user/profiles/{username}")
    public Result<UserProfilesVO> getUserProfiles(@PathVariable String username, HttpServletRequest request) {
        Integer requesterId = (Integer) request.getAttribute("userId");
        try {
            log.info("getUserProfiles: requesterId={}, username={}", requesterId, username);
            return Result.success(userService.getUserProfiles(requesterId, username));
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 关注用户
     *
     * @param followingId
     * @param request
     * @return
     */
    @PostMapping("/user/follow/{followingId}")
    public Result<String> followUser(@PathVariable Integer followingId, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        try {
            log.info("followUser: userId={}, followingId={}", userId, followingId);
            return userService.followUser(userId, followingId) ?
                    Result.success("关注成功") :
                    Result.success("取消关注成功");

        } catch (Exception e) {
            log.error("关注失败: {}", e.getMessage());
            return Result.error("关注失败");
        }
    }

    /**
     * 更新用户信息
     *
     * @param userId
     * @param userProfileDTO
     * @return
     */
    @PutMapping("/user/update/{userId}")
    public Result<UserProfilesVO> updateUserProfile(@PathVariable Integer userId,
                                                    @RequestBody UserProfileDTO userProfileDTO,
                                                    HttpServletRequest request) {
        // 判断有无权限
        if (request.getAttribute("userId") == null || !userId.equals(request.getAttribute("userId"))) {
            return Result.error("无操作权限");
        }
        try {
            log.info("updateUserProfile: userId={}, userName={}, bio={}", userId, userProfileDTO.getUserName(), userProfileDTO.getBio());
            return Result.success(
                    userService.updateUserProfile(
                            userId,
                            userProfileDTO.getUserName(),
                            userProfileDTO.getBio(),
                            userProfileDTO.getAvatarBase64()
                    )
            );
        } catch (InvalidUserIdException | UsernameAlreadyExistsException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("未知错误");
        }
    }

    /**
     * 检查用户名是否已存在
     *
     * @param userName
     * @return
     */
    @GetMapping("/user/checkUsername/{userName}")
    public Result<String> checkUsername(@PathVariable String userName, HttpServletRequest request) {
        try {
            log.info("checkUsername: userName={}", userName);
            Integer userId = (Integer) request.getAttribute("userId");
            return Result.success(
                    userService.isUserExist(userId, userName) ?
                            "用户名已经被占用" :
                            "用户名可用");
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage());
            return Result.error("检查用户名失败");
        }
    }

    /**
     * 搜索用户
     *
     * @param userName
     * @return
     */
    @GetMapping("/user/search")
    public Result<List<User>> searchUsers(@RequestParam String userName, HttpServletRequest request) {
        try {
            log.info("searchUsers: userName={}", userName);
            return Result.success(userService.searchUsers(userName));
        } catch (Exception e) {
            log.error("搜索用户失败: {}", e.getMessage());
            return Result.error("搜索用户失败");
        }
    }

    @GetMapping("/user/followingList")
    public Result<List<UserProfilesVO>> getFollowingList(@RequestParam(defaultValue = "10") Integer size,
                                                         @RequestParam Integer userId,
                                                         @RequestParam(required = false) Integer lastUserId,
                                                         HttpServletRequest request) {
        Integer operatorId = (Integer) request.getAttribute("userId");
        return Result.success(userService.getFollowingList(operatorId, userId, size, lastUserId));
    }

    @GetMapping("/user/fansList")
    public Result<List<UserProfilesVO>> getFansList(@RequestParam(defaultValue = "10") Integer size,
                                                    @RequestParam Integer userId,
                                                    @RequestParam(required = false) Integer lastUserId,
                                                    HttpServletRequest request) {
        Integer operatorId = (Integer) request.getAttribute("userId");
        return Result.success(userService.getFansList(operatorId, userId, size, lastUserId));
    }
}

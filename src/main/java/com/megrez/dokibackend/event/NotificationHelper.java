package com.megrez.dokibackend.event;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.controller.WebSocketController;
import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.entity.Notification;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.mapper.CommentMapper;
import com.megrez.dokibackend.mapper.UserMapper;
import com.megrez.dokibackend.mapper.VideoMapper;
import com.megrez.dokibackend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationHelper {
    private final UserMapper userMapper;
    private final VideoMapper videoMapper;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;
    private final WebSocketController webSocketController;

    public NotificationHelper(UserMapper userMapper,
                              NotificationService notificationService,
                              WebSocketController webSocketController,
                              VideoMapper videoMapper,
                              CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.notificationService = notificationService;
        this.webSocketController = webSocketController;
        this.videoMapper = videoMapper;
        this.commentMapper = commentMapper;
    }

    /**
     * 处理点赞事件
     *
     * @param payload
     */
    public void pushLikeNotification(Payload payload) {
        Integer targetUserId; // 目标用户 ID
        String targetObject; // 目标对象（视频或评论）
        CommentsDTO commentById = null; // 目标评论
        Video videoById;    // 目标视频

        // 如果载荷没有传递视频ID，则说明是评论点赞事件
        if (payload.getVideoId() == null) {
            targetObject = "评论";
            // 根据载荷提供的目标评论ID，查找目标评论
            commentById = commentMapper.getCommentById(payload.getCommentId());
            // 根据结果，获取目标评论的作者的ID
            targetUserId = commentById.getUserId();
            // 查找这条评论所在的视频
            videoById = videoMapper.getVideoById(commentById.getVideoId());
        } else {
            targetObject = "视频";
            // 根据载荷提供的目标视频ID，查找目标视频
            videoById = videoMapper.getVideoById(payload.getVideoId());
            // 根据结果，获取目标视频的作者的ID
            targetUserId = videoById.getUserId();
        }

        // 初始化通知对象
        Notification notification = new Notification();

        // 推送目标的ID
        notification.setUserId(targetUserId);
        // 通知类型
        notification.setType(payload.getType().toString());

        // 通知内容
        User operator = userMapper.getUserById(payload.getOperatorId());
        notification.setMessage(operator.getUserName() + "赞了你的" + targetObject);

        // 设置是否已读
        notification.setIsRead(false);

        // 设置生产者，操作发生的视频ID
        notification.setOperatorId(payload.getOperatorId());
        notification.setVideoId(videoById.getId());
        // 如果是评论点赞事件，则设置评论ID
        notification.setCommentId(commentById != null ? commentById.getId() : null);

        // 添加记录到数据库
        notificationService.addNotification(notification);
        // 推送通知，这里10000代表是一条点赞的通知
        webSocketController.pushMessageToUser(targetUserId, "10000");
    }

    /**
     * 处理评论事件
     *
     * @param payload
     */
    public void pushCommentNotification(Payload payload) {
        // 构建通知对象
        Notification notification = new Notification();

        // 查找生产者的用户名
        String operatorName = userMapper.getUserById(payload.getOperatorId()).getUserName();

        // 如果评论为空，说明是顶级评论，推送给视频作者
        if (payload.getCommentId() == null) {
            // 根据视频ID，查找视频作者信息
            Video videoById = videoMapper.getVideoById(payload.getVideoId());
            notification.setUserId(videoById.getUserId());
        } else {
            //  如果是回复则推送给评论所有者
            CommentsDTO commentById = commentMapper.getCommentById(payload.getCommentId());
            notification.setUserId(commentById.getUserId());
        }
        // 把message填充为评论内容，这里和前端约定：如果返回的评论ID为空
        // 这说明是顶级评论，渲染"XXX评论了您的视频"
        notification.setMessage(payload.getMessage());
        notification.setType(NotificationType.COMMENT.name());
        notification.setIsRead(false);
        notification.setOperatorId(payload.getOperatorId());
        notification.setVideoId(payload.getVideoId());
        notification.setCommentId(payload.getCommentId() == null ? null : payload.getCommentId());
        // 存储通知到数据库
        notificationService.addNotification(notification);
        // 推送，这里10001代表一条评论的通知
        webSocketController.pushMessageToUser(notification.getUserId(), "10001");
    }

    public void pushDirectMessageNotification(Payload payload) {
        // 构建通知对象
        Notification notification = new Notification();
        notification.setUserId(payload.getTargetUserId());
        notification.setType(NotificationType.DIRECT_MESSAGE.name());
        notification.setMessage(payload.getMessage());
        notification.setIsRead(false);
        notification.setOperatorId(payload.getOperatorId());
        notification.setCreatedAt(LocalDateTime.now());
        // 存储通知到数据库
        notificationService.addNotification(notification);
        // 推送，这里20000代表一条评论的通知
        webSocketController.pushMessageToUser(notification.getUserId(), "20000");
    }
}

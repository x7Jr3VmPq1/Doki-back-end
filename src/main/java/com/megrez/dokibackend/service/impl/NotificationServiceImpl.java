package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.entity.Notification;
import com.megrez.dokibackend.mapper.NotificationMapper;
import com.megrez.dokibackend.service.NotificationService;
import com.megrez.dokibackend.vo.NotificationVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    /**
     * 添加通知
     *
     * @param notification
     */
    @Override
    public void addNotification(Notification notification) {
        notificationMapper.addNotification(notification);
    }

    /**
     * 根据用户id和通知类型获取通知
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public List<NotificationVO> getNotificationByUserIdAndType(Integer userId, String type) {
        // 判断typed的合法性，不合法抛出异常
        try {
            NotificationType.valueOf(type.toUpperCase());
            return notificationMapper.getNotificationsByUserIdAndType(userId, type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不合法的参数");
        }
    }
}

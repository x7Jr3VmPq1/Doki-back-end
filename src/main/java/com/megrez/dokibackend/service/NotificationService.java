package com.megrez.dokibackend.service;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.entity.Notification;
import com.megrez.dokibackend.vo.NotificationVO;

import java.util.List;

public interface NotificationService {
    /**
     * 添加通知
     *
     * @param notification
     */
    void addNotification(Notification notification);

    /**
     * 根据用户id和通知类型获取通知
     *
     * @param userId
     * @param type
     * @return
     */
    List<NotificationVO> getNotificationByUserIdAndType(Integer userId, String type);
}

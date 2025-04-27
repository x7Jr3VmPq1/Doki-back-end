package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.entity.Notification;
import com.megrez.dokibackend.vo.NotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface NotificationMapper {
    /**
     * 插入通知
     *
     * @param notification
     */
    void addNotification(Notification notification);

    /**
     * 根据用户ID获取对应类型的通知
     *
     * @param userId
     * @param type
     * @return
     */
    List<NotificationVO> getNotificationsByUserIdAndType(@Param("userId") Integer userId,
                                                         @Param("type") String type);
}

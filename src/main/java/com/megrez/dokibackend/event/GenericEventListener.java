package com.megrez.dokibackend.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GenericEventListener {

    private final NotificationHelper notificationHelper;

    public GenericEventListener(NotificationHelper notificationHelper) {
        this.notificationHelper = notificationHelper;
    }

    @EventListener
    public void handleGenericEvent(Payload payload) {
        // 根据事件类型处理不同的逻辑
        switch (payload.getType()) {
            case LIKE:
                notificationHelper.pushLikeNotification(payload);
                break;
            case COMMENT:
                notificationHelper.pushCommentNotification(payload);
                break;
            case DIRECT_MESSAGE:
                notificationHelper.pushDirectMessageNotification(payload);
                break;
                // 其他类型的事件
            default:
//                throw new UnsupportedOperationException("未知类型: " + payload.getType());
                return;
        }
    }
}

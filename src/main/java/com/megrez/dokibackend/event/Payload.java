package com.megrez.dokibackend.event;

import com.megrez.dokibackend.common.NotificationType;
import lombok.Data;

@Data
public class Payload {
    // 推送目标ID
    private final Integer targetUserId;
    // 生产者ID
    private final Integer operatorId;
    // 消息类型
    private final NotificationType type;
    // 消息内容(如果有)
    private final String message;
    // 评论ID(指的是发生操作的对象，比如被点赞的评论，被回复的评论)
    private final Integer commentId;
    // 视频ID
    private final Integer videoId;
}

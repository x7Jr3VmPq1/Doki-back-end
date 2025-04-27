package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.service.NotificationService;
import com.megrez.dokibackend.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class NotificationController {
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/{type}")
    public Result<List<NotificationVO>> getNotificationsByUserIdAndType(HttpServletRequest request, @PathVariable String type) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("{}拉取了通知列表，类型为：{}", userId, type);
        try {
            return Result.success(notificationService.getNotificationByUserIdAndType(userId, type));
        } catch (IllegalArgumentException e) {
            return Result.error("不合法的参数");
        }
    }
}

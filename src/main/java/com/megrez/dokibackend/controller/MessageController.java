package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.dto.DirectMessageDTO;
import com.megrez.dokibackend.service.MessageService;
import com.megrez.dokibackend.vo.MessageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 拉取聊天列表
     *
     * @param request
     * @return
     */

    @GetMapping("/messages")
    public Result<List<MessageVO>> getMessagesByUserId(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("用户 {} 拉取聊天列表...", userId);
        return Result.success(messageService.getMessagesByUserId(userId));
    }

    /**
     * 向指定用户发送消息
     *
     * @param directMessageDTO // 消息体
     */
    @PostMapping("/messages")
    public Result<String> sendMessage(HttpServletRequest request,
                                      @RequestBody DirectMessageDTO directMessageDTO) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("{} 向{}发送消息...{}", directMessageDTO.getUserId(),
                directMessageDTO.getTargetUserId(),
                directMessageDTO.getMessageString());
        directMessageDTO.setUserId(userId);
        messageService.sendMessage(directMessageDTO);
        return Result.success("消息发送成功");
    }
}

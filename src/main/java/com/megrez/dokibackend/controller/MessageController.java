package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.dto.DirectMessageDTO;
import com.megrez.dokibackend.entity.Message;
import com.megrez.dokibackend.service.MessageService;
import com.megrez.dokibackend.vo.ConversationVO;
import com.megrez.dokibackend.vo.MessageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class MessageController {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 拉取会话列表
     *
     * @param request
     * @return
     */
    @GetMapping("/conversations")
    public Result<List<ConversationVO>> getConversationListByUserId(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("用户 {} 拉取会话列表...", userId);
        return Result.success(messageService.getConversationListByUserId(userId));
    }

    /**
     * 根据指定的ID获取聊天记录
     *
     * @param request
     * @return
     */
    @GetMapping("/getMessagesByConversationId")
    public Result<List<Message>> getMessagesByConversationId(HttpServletRequest request, @RequestParam String conversationId) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("用户 {} 拉取会话 {} 的聊天记录...", userId, conversationId);
        return Result.success(messageService.getMessagesByConversationId(conversationId, userId));
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
    public Result<Message> sendMessage(HttpServletRequest request, @RequestBody DirectMessageDTO directMessageDTO) throws IOException {
        Integer userId = (Integer) request.getAttribute("userId");
        directMessageDTO.setUserId(userId);
        return Result.success(messageService.sendMessage(directMessageDTO));
    }
}

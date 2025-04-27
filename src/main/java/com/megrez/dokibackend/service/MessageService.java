package com.megrez.dokibackend.service;

import com.megrez.dokibackend.dto.DirectMessageDTO;
import com.megrez.dokibackend.entity.Message;
import com.megrez.dokibackend.vo.MessageVO;

import java.util.List;
import java.util.Map;

public interface MessageService {
    /**
     * 根据用户ID获取消息
     *
     * @param userId
     * @return
     */
    List<MessageVO> getMessagesByUserId(Integer userId);

    /**
     * 发送消息
     *
     * @param directMessageDTO // 消息体
     */
    void sendMessage(DirectMessageDTO directMessageDTO);
}

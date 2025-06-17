package com.megrez.dokibackend.service;

import com.megrez.dokibackend.dto.DirectMessageDTO;
import com.megrez.dokibackend.entity.Message;
import com.megrez.dokibackend.vo.ConversationVO;
import com.megrez.dokibackend.vo.MessageVO;

import java.io.IOException;
import java.util.List;

public interface MessageService {
    /**
     * 获取会话列表
     *
     * @param userId
     * @return
     */
    List<ConversationVO> getConversationListByUserId(Integer userId);

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
     * @return
     */
    Message sendMessage(DirectMessageDTO directMessageDTO) throws IOException;

    /**
     * 根据会话ID获取消息
     *
     * @param conversationId
     * @param userId
     * @return
     */
    List<Message> getMessagesByConversationId(String conversationId, Integer userId);
}

package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.dto.DirectMessageDTO;
import com.megrez.dokibackend.entity.Conversation;
import com.megrez.dokibackend.entity.Message;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.event.Payload;
import com.megrez.dokibackend.mapper.MessageMapper;
import com.megrez.dokibackend.service.MessageService;
import com.megrez.dokibackend.vo.MessageVO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final ApplicationEventPublisher eventPublisher;

    public MessageServiceImpl(MessageMapper messageMapper,
                              ApplicationEventPublisher eventPublisher) {
        this.messageMapper = messageMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 根据用户id获取消息
     *
     * @param userId // 用户id
     */
    @Override
    @Transactional
    public List<MessageVO> getMessagesByUserId(Integer userId) {
        // 获取会话列表
        List<Conversation> conversations = messageMapper.getConversationListByUserId(userId);
        // 遍历会话列表，获取每个会话的详细信息
        List<MessageVO> messageVOS = new ArrayList<>();
        for (Conversation conversation : conversations) {
            // 根据会话ID获取消息列表
            List<Message> messages = messageMapper.getMessagesByConversationId(conversation.getId(), userId);
            // 如果消息列表为空，则跳过当前会话
            if (messages.isEmpty()) {
                continue;
            }
            // 把自己发的消息都设置成已读
            for (Message message : messages) {
                if (message.getSenderId().equals(userId)) {
                    message.setRead(true);
                }
            }
            MessageVO messageVO = new MessageVO();
            // 如果是私聊，查找对方的用户信息，并把对应的值赋给messageVO中的字段
            if (conversation.getConversationType().equals("private")) {
                // 根据会话ID查找会话持有人，过滤掉自己
                List<User> userInfoByConversationId = messageMapper.getUserInfoByConversationId(conversation.getId());
                for (User user : userInfoByConversationId) {
                    if (!user.getId().equals(userId)) {
                        // 设置对话目标用户名
                        messageVO.setUsername(user.getUserName());
                        // 设置对话目标用户头像
                        messageVO.setAvatarUrl(user.getAvatarUrl());
                        // 设置对话目标用户ID
                        messageVO.setUserId(user.getId());
                    }
                }
            }
            // 设置会话ID
            messageVO.setConversationId(conversation.getId());
            // 设置消息列表
            messageVO.setMessages(messages);
            // 将messageVO添加到列表中
            messageVOS.add(messageVO);
        }

        // 给元素排序，依据是messages数组中的最后一条消息的时间
        messageVOS.sort(Comparator.comparing(
                (MessageVO messageVO) -> messageVO.getMessages()
                        .get(messageVO.getMessages().size() - 1)
                        .getSentAt()
        ).reversed());

        return messageVOS;
    }

    /**
     * 发送消息
     *
     * @param directMessageDTO // 消息体
     */

    @Override
    @Transactional
    public void sendMessage(DirectMessageDTO directMessageDTO) {
        // 先查找会话ID
        String conversationId = directMessageDTO.getConversationId();
        // 如果会话ID为空，则创建一个会话
        if (conversationId == null) {
            // 构建一个小数组存储两个用户的ID(需要为两个用户分别建立会话)
            Integer[] userIds = {directMessageDTO.getUserId(), directMessageDTO.getTargetUserId()};
            conversationId = UUID.randomUUID().toString();
            for (Integer id : userIds) {
                Conversation conversation = new Conversation();
                conversation.setId(conversationId);
                conversation.setConversationType("private");
                conversation.setUserId(id);
                conversation.setLastDeletedAt(null);
                messageMapper.createConversation(conversation);
            }
        }
        // 创建消息对象，并插入数据库
        Message message = new Message();
        message.setSenderId(directMessageDTO.getUserId());
        message.setMessage(directMessageDTO.getMessageString());
        message.setSentAt(LocalDateTime.now());
        message.setAttachmentUrl(null);
        message.setReplyToId(null);
        message.setConversationId(conversationId);
        messageMapper.insertMessage(message);

        // 向消息目标推送通知
        eventPublisher.publishEvent(new Payload(
                directMessageDTO.getTargetUserId(),
                directMessageDTO.getUserId(),
                NotificationType.DIRECT_MESSAGE,
                directMessageDTO.getMessageString(),
                null,
                null
        ));
    }
}

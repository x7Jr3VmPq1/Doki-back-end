package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.entity.Conversation;
import com.megrez.dokibackend.entity.Message;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.vo.ConversationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * 添加消息记录
     *
     * @param message
     */
    void insertMessage(Message message);

    /**
     * 根据用户id获取会话列表
     *
     * @param userId
     * @return
     */
    List<Conversation> getConversationListByUserId(Integer userId);

    /**
     * 根据用户id获取消息记录
     *
     * @param userId         // 用户id
     * @param conversationId // 会话id
     * @return
     */
    List<Message> getMessagesByConversationId(@Param("conversationId") String conversationId,
                                              @Param("userId") Integer userId);

    /**
     * 创建会话
     *
     * @param conversation
     */
    void createConversation(Conversation conversation);

    /**
     * 根据用户id获取会话id
     *
     * @param userId
     * @param targetUserId
     * @return
     */
    String getConversationByUsers(@Param("userId") Integer userId,
                                  @Param("targetUserId") Integer targetUserId);

    /**
     * 根据会话id获取用户信息
     *
     * @param conversationId 会话id
     * @param userId         当前用户id
     * @return 用户信息
     */
    User getUserInfoByConversationId(@Param("conversationId") String conversationId,
                                           @Param("userId") Integer userId);

    /**
     * 标记所有消息为已读
     *
     * @param conversationId // 会话id
     * @param userId         // 用户id
     */
    void markAllMessagesAsRead(@Param("conversationId") String conversationId, @Param("userId") Integer userId);

    /**
     * 更新会话信息
     *
     * @param conversationId // 会话id
     * @param message        // 最新消息
     * @param sentAt         // 最新消息发送时间
     */
    void updateConversation(@Param("id") String conversationId,
                            @Param("lastMessageContent") String message,
                            @Param("lastMessageAt") LocalDateTime sentAt);
}

package com.megrez.dokibackend.service;

import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.vo.CommentVO;

import java.util.List;

/**
 * CommentService 接口定义了与评论相关的业务逻辑操作。
 * 该接口提供了添加、更新、删除评论以及查询评论的功能。
 */
public interface CommentService {

    /**
     * 添加一条新的评论。
     *
     * @param comment 包含评论信息的 SingleCommentDTO 对象
     */
    SingleCommentDTO addComment(SingleCommentDTO comment);

    /**
     * 更新指定评论的点赞数。
     *
     * @param commentId 评论的唯一标识符
     * @param likeCount 新的点赞数
     * @param userId    用户的唯一标识符
     */
    void updateCommentLikeCount(Integer userId, Integer commentId, Integer likeCount);

    /**
     * 根据评论的唯一标识符删除评论。
     *
     * @param commentId 评论的唯一标识符
     */
    void delCommentsById(Integer commentId);

    /**
     * 根据评论的唯一标识符获取评论的详细信息。
     *
     * @param commentId 评论的唯一标识符
     * @return 返回包含评论详细信息的 CommentVO 对象
     */
    CommentVO getCommentById(Integer commentId);

    /**
     * 根据用户的唯一标识符获取该用户的所有评论。
     *
     * @param userId 用户的唯一标识符
     * @return 返回包含该用户所有评论的 CommentVO 列表
     */
    List<CommentVO> getCommentsByUserId(Integer userId);

    /**
     * 根据视频的唯一标识符获取该视频的所有评论。
     *
     * @param videoId 视频的唯一标识符
     * @return 返回包含该视频所有评论的 CommentVO 列表
     */
    List<CommentVO> getCommentsByVideoId(Integer videoId, Integer userId, Integer cursor);

    /**
     * 根据父评论的唯一标识符获取所有二级评论。
     *
     * @param parentCommentId 父评论的唯一标识符
     * @return 返回包含所有二级评论的 CommentVO 列表
     */
    List<CommentVO> getSecondLevelComments(Integer parentCommentId, Integer userId);

    /**
     * 根据视频的唯一标识符获取该视频的评论数量。
     *
     * @param videoId 视频的唯一标识符
     * @return 返回该视频的评论数量
     */
    Integer getCommentsNumberByVideoId(Integer videoId);

    /**
     * 添加点赞记录。
     *
     * @param userId    用户的唯一标识符
     * @param commentId 评论的唯一标识符
     */
    boolean addLikeRecord(Integer userId, Integer commentId);

}

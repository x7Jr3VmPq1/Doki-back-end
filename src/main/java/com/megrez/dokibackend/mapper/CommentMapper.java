package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CommentMapper 接口用于处理与评论相关的数据库操作。
 * 该接口定义了多个方法，用于获取、添加、更新和删除评论。
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据视频ID获取评论数量。
     *
     * @param videoId 视频的唯一标识符。
     * @return 返回指定视频的评论数量。
     */
    int getCommentsNumberByVideoId(@Param("videoId") int videoId);

    /**
     * 根据视频ID获取评论列表。
     *
     * @param videoId 视频的唯一标识符。
     * @return 返回指定视频的评论列表。
     */
    List<CommentsDTO> getCommentsByVideoId(@Param("videoId") int videoId);

    /**
     * 根据父评论ID获取二级评论列表。
     *
     * @param parentCommentId 父评论的唯一标识符。
     * @return 返回指定父评论下的二级评论列表。
     */
    List<CommentsDTO> getSecondLevelComments(@Param("parentCommentId") int parentCommentId);

    /**
     * 添加一条新评论。
     *
     * @param singleCommentDTO 包含评论信息的DTO对象。
     * @return 返回受影响的行数，通常为1表示添加成功。
     */
    int addComment(SingleCommentDTO singleCommentDTO);

    /**
     * 更新评论的点赞数。
     *
     * @param commentId 评论的唯一标识符。
     * @param likeCount 新的点赞数。
     * @return 返回受影响的行数，通常为1表示更新成功。
     */
    int updateCommentLikeCount(@Param("commentId") int commentId, @Param("likeCount") int likeCount);

    /**
     * 更新评论的点踩数。
     *
     * @param commentId    评论的唯一标识符。
     * @param dislikeCount 新的点踩数。
     * @return 返回受影响的行数，通常为1表示更新成功。
     */
    int updateCommentDislikeCount(@Param("commentId") int commentId, @Param("dislikeCount") int dislikeCount);

    /**
     * 根据评论ID获取评论详情。
     *
     * @param commentId 评论的唯一标识符。
     * @return 返回指定评论的详细信息。
     */
    CommentsDTO getCommentById(@Param("commentId") int commentId);

    /**
     * 根据用户ID获取评论列表。
     *
     * @param userId 用户的唯一标识符。
     * @return 返回指定用户的所有评论列表。
     */
    List<CommentsDTO> getCommentsByUserId(@Param("userId") int userId);

    /**
     * 根据评论ID删除评论。
     *
     * @param commentId 评论的唯一标识符。
     * @return 返回受影响的行数，通常为1表示删除成功。
     */
    int delCommentsById(@Param("commentId") int commentId);

    /**
     * 添加点赞记录。
     *
     * @param userId
     * @param commentId
     */
    void addLikeRecord(@Param("userId") int userId, @Param("commentId") int commentId);

    /**
     * 删除点赞记录
     *
     * @param userId
     * @param commentId
     */
    void delLikeRecord(@Param("userId") int userId, @Param("commentId") int commentId);

    /**
     * 判断点赞记录是否存在
     *
     * @param userId
     * @param commentId
     * @return
     */
    boolean isLikeRecordExist(@Param("userId") int userId, @Param("commentId") int commentId);
}

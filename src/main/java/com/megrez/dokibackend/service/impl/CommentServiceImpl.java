package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.common.LocalFilesPath;
import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.controller.WebSocketController;
import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.entity.Notification;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.event.Payload;
import com.megrez.dokibackend.mapper.CommentMapper;
import com.megrez.dokibackend.mapper.UserMapper;
import com.megrez.dokibackend.mapper.VideoMapper;
import com.megrez.dokibackend.service.CommentService;
import com.megrez.dokibackend.service.NotificationService;
import com.megrez.dokibackend.vo.CommentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    public CommentServiceImpl(CommentMapper commentMapper,
                              ApplicationEventPublisher eventPublisher) {
        this.commentMapper = commentMapper;
        this.eventPublisher = eventPublisher;
    }


    /**
     * 添加评论
     *
     * @param comment 包含评论信息的 SingleCommentDTO 对象
     */
    @Override
    public SingleCommentDTO addComment(SingleCommentDTO comment) {
        // 判断有无图片，有图片，把图片传到文件服务器上
        String imageBASE64 = comment.getImgUrl();
        if (StringUtils.hasText(imageBASE64)) {
            try {
                // 解析 Base64 字符串，去掉头部的 `data:image/jpeg;base64,` 部分
                String base64Data = imageBASE64.split(",")[1];

                // 将 Base64 字符串解码为字节数组
                byte[] imgBytes = Base64Utils.decodeFromString(base64Data);

                // 生成唯一的文件名
                String fileName = UUID.randomUUID().toString() + ".jpg";

                // 确保目录存在
                Path directory = Paths.get(LocalFilesPath.commentImageUploadPath);
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                // 保存图片到本地文件系统
                Path filePath = directory.resolve(fileName);
                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    fos.write(imgBytes);
                }
                // 设置评论图片的 URL
                String imgUrl = FileServerURL.commentImageFilesPath + fileName;
                comment.setImgUrl(imgUrl);

            } catch (IOException e) {
                throw new RuntimeException("图片上传失败", e);
            }
        }

        commentMapper.addComment(comment);
        // 发布评论事件
        eventPublisher.publishEvent(new Payload(
                null,
                comment.getUserId(),
                NotificationType.COMMENT,
                comment.getContent(),
                comment.getParentCommentId() == null ? null : comment.getParentCommentId(),
                comment.getVideoId()
        ));
        // 返回添加的评论
        return comment;
    }

    /**
     * 更新评论的点赞数
     *
     * @param userId    用户的唯一标识符
     * @param commentId 评论的唯一标识符
     * @param likeCount 新的点赞数
     */
    @Override
    public void updateCommentLikeCount(Integer userId, Integer commentId, Integer likeCount) {
        // TODO 需要维护中间表处理点赞关系
        commentMapper.updateCommentLikeCount(commentId, likeCount);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论的唯一标识符
     */
    @Override
    public void delCommentsById(Integer commentId) {
        commentMapper.delCommentsById(commentId);
    }

    @Override
    public CommentVO getCommentById(Integer commentId) {
        return null;
    }

    @Override
    public List<CommentVO> getCommentsByUserId(Integer userId) {
        return List.of();
    }

    /**
     * 根据视频的ID获取评论
     *
     * @param videoId 视频的唯一标识符
     * @return
     */
    @Override
    public List<CommentVO> getCommentsByVideoId(Integer videoId, Integer userId, Integer cursor) {
        // 根据视频的ID获取评论
        List<CommentsDTO> comments = commentMapper.getCommentsByVideoId(videoId, cursor);
        // 遍历评论列表，将评论转换为 CommentVO 对象，并添加到结果列表中
        List<CommentVO> commentVOList = new ArrayList<>();
        comments.forEach(commentsDTO -> {
                    CommentVO commentVO = new CommentVO();
                    // 查询用户是否是否点赞，如果userId有值再设置这个状态
                    if (userId != null) {
                        commentVO.setLiked(commentMapper.isLikeRecordExist(userId, commentsDTO.getId()));
                    }
                    BeanUtils.copyProperties(commentsDTO, commentVO);
                    // 递归获取二级评论
                    commentVO.setReplies(getSecondLevelComments(commentsDTO.getId(), userId));
                    commentVOList.add(commentVO);
                }
        );
        return commentVOList;
    }

    /**
     * 根据父评论的ID获取二级评论
     *
     * @param parentCommentId 父评论的唯一标识符
     * @param userId
     * @return
     */
    @Override
    public List<CommentVO> getSecondLevelComments(Integer parentCommentId, Integer userId) {
        List<CommentVO> commentVOList = new ArrayList<>();
        commentMapper.getSecondLevelComments(parentCommentId).forEach(commentsDTO -> {
            CommentVO commentVO = new CommentVO();
            // 查询用户是否是否点赞，如果userId有值再设置这个状态
            if (userId != null) {
                commentVO.setLiked(commentMapper.isLikeRecordExist(userId, commentsDTO.getId()));
            }
            BeanUtils.copyProperties(commentsDTO, commentVO);
            commentVOList.add(commentVO);
        });
        return commentVOList;
    }

    /**
     * 根据视频的ID获取评论的数量
     *
     * @param videoId 视频的唯一标识符
     * @return
     */
    @Override
    public Integer getCommentsNumberByVideoId(Integer videoId) {
        return 0;
    }

    /**
     * 为评论点赞增加点赞记录
     *
     * @param userId    用户的唯一标识符
     * @param commentId 评论的唯一标识符
     */
    @Override
    public boolean addLikeRecord(Integer userId, Integer commentId) {
        CommentsDTO commentById = commentMapper.getCommentById(commentId);
        // 检查是否已存在点赞记录
        if (commentMapper.isLikeRecordExist(userId, commentId)) {
            // 如果存在点赞记录，则删除点赞记录
            commentMapper.delLikeRecord(userId, commentId);
            return false;
        } else {
            // 如果不存在点赞记录，则添加点赞记录
            commentMapper.addLikeRecord(userId, commentId);
            // 如果是自己，不要推送任何消息
            if (userId.equals(commentById.getUserId())) {
                return true;
            }

            // 发布点赞事件
            eventPublisher.publishEvent(new Payload(
                    null,
                    userId,
                    NotificationType.LIKE,
                    null,
                    commentId,
                    null));
            return true;
        }
    }
}

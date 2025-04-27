package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.exception.DuplicateLikeException;
import com.megrez.dokibackend.service.CommentService;
import com.megrez.dokibackend.vo.CommentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * CommentController 类用于处理与评论相关的HTTP请求。
 * 该类通过REST API提供获取视频评论的功能。
 */
@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final CommentService commentService;


    /**
     * 构造函数，用于注入CommentService依赖。
     *
     * @param commentService 评论服务类，用于处理与评论相关的业务逻辑。
     */
    public CommentController(CommentService commentService, WebSocketController webSocketController) {
        this.commentService = commentService;
    }

    /**
     * 根据视频ID获取该视频的所有评论。
     *
     * @param videoId 视频的唯一标识符。
     * @return 返回一个包含该视频所有评论的列表，每个评论以CommentVO对象表示。
     */
    @GetMapping("/video/comments/{videoId}")
    public Result<List<CommentVO>> getCommentsByVideoId(@PathVariable Integer videoId, HttpServletRequest request) {
        // 从请求头中的token解析出用户id
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("getCommentsByVideoId: videoId={}, userId={}", videoId, userId);
        // 调用服务层方法获取该视频的所有评论
        List<CommentVO> comments = commentService.getCommentsByVideoId(videoId, userId);
        return Result.success(comments);
    }

    /**
     * 添加一条评论。
     *
     * @param comment 评论信息，包括视频ID、用户ID、评论内容等。
     * @return 返回一个包含成功消息的Result对象。
     */
    @PostMapping("/video/comments/add")
    public Result<String> addComment(@RequestBody @Valid SingleCommentDTO comment) {
        log.info("addComment: comment={}", comment);
        commentService.addComment(comment);
        return Result.success("评论成功");
    }

    /**
     * 删除一条评论。
     *
     * @param commentId
     * @return
     */
    @DeleteMapping("/video/comments/del/{commentId}")
    public Result<String> delCommentsById(@PathVariable Integer commentId) {
        log.info("delCommentsById: commentId={}", commentId);
        try {
            commentService.delCommentsById(commentId);
            return Result.success("删除评论成功");
        } catch (Exception e) {
            return Result.error("删除评论失败");
        }
    }

    /**
     * 为评论点赞。
     * 该方法会检查用户是否已登录，如果未登录则返回错误信息。
     * 如果用户已登录，则调用服务层方法为指定评论添加点赞记录。
     * 如果检测到重复点赞（即用户已为该评论点过赞），则捕获DuplicateLikeException并返回相应的错误信息。
     *
     * @param commentId 评论的唯一标识符。
     * @param request   HttpServletRequest对象，用于获取当前用户的ID。
     * @return 返回一个包含操作结果的Result对象，成功时返回"点赞成功"，失败时返回相应的错误信息。
     */
    @PostMapping("/video/comments/like/{commentId}")
    public Result<String> addLikeRecord(@PathVariable Integer commentId, HttpServletRequest request) {
        // 对userId判空
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("addLikeRecord: commentId={}, userId={}", commentId, userId);
        try {
            return commentService.addLikeRecord(userId, commentId) ?
                    Result.success("点赞成功") :
                    Result.success("取消点赞成功");
        } catch (DuplicateLikeException e) {
            return Result.error("您已经点过赞了");
        }
    }
}

package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.dto.VideoDTO;
import com.megrez.dokibackend.dto.VideoUploadInfoDTO;
import com.megrez.dokibackend.exception.DuplicateLikeException;
import com.megrez.dokibackend.vo.VideoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.megrez.dokibackend.service.VideosService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class VideosController {
    private static final Logger log = LoggerFactory.getLogger(VideosController.class);
    public final VideosService videosService;

    public VideosController(VideosService videosService) {
        this.videosService = videosService;
    }

    /**
     * 获取所有视频
     *
     * @param request
     * @return
     */
    @GetMapping("/videos")
    public Result<List<VideoVO>> getAllVideos(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("getAllVideos: userId={}", userId);
        List<VideoVO> videoDTOList = videosService.getAllVideos(userId);
        return Result.success(videoDTOList);
    }

    /**
     * 根据关键词搜索视频
     *
     * @param keyword
     * @param request
     * @return
     */
    @GetMapping("/videos/search")
    public Result<List<VideoVO>> searchVideosByKeyword(@RequestParam String keyword, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("searchVideosByKeyword: userId={},keyword={}", userId, keyword);
        List<VideoVO> videoDTOList = null;
        try {
            videoDTOList = videosService.searchVideosByKeyword(keyword, userId);
        } catch (IOException e) {
            return Result.error("搜索失败");
        }
        return Result.success(videoDTOList);
    }

    /**
     * 点赞视频
     *
     * @param videoId
     * @param request
     * @return
     */
    @PostMapping("/videos/like/{videoId}")
    public Result<String> addLikeRecord(@PathVariable Integer videoId, HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("addLikeRecord: videoId={}, userId={}", videoId, userId);
        try {
            return videosService.addlikeRecord(userId, videoId) ?
                    Result.success("点赞成功") :
                    Result.success("取消点赞成功");
        } catch (DuplicateLikeException e) {
            return Result.error("您已经点过赞了");
        }
    }

    /**
     * 收藏视频
     *
     * @param videoId
     * @param request
     * @return
     */
    @PostMapping("/videos/collect/{videoId}")
    public Result<String> addCollectRecord(@PathVariable Integer videoId, HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("addCollectRecord: videoId={}, userId={}", videoId, userId);
        try {
            return videosService.addCollectRecord(userId, videoId) ?
                    Result.success("收藏成功") :
                    Result.success("取消收藏成功");
        } catch (DuplicateLikeException e) {
            return Result.error("您已经收藏过了");
        }
    }

    /**
     * 上传视频接口
     *
     * @param video
     * @param request
     * @return
     */
    @PostMapping("/videos/upload")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile video, HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("uploadVideo:userId={},videoSize={}", userId, video.getSize());
        try {
            videosService.uploadVideo(video, userId);
            return Result.success("上传成功");
        } catch (Exception e) {
            return Result.error("上传失败");
        }
    }

    /**
     * 发布视频接口
     *
     * @param videoUploadInfoDTO 视频上传信息
     * @param request            HTTP请求
     * @return 发布结果
     */
    @PostMapping("/videos/publish")
    public Result<String> publishVideo(@RequestBody VideoUploadInfoDTO videoUploadInfoDTO, HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("publishVideo: userId={},info={}", userId, videoUploadInfoDTO);
        try {
            videosService.publishVideo(videoUploadInfoDTO, userId);
            return Result.success("发布成功");
        } catch (Exception e) {
            return Result.error("发布失败");
        }
    }

    /**
     * 删除视频
     *
     * @param videoId
     * @param request
     * @return
     */
    @DeleteMapping("/videos/del/{videoId}")
    public Result<String> delVideoByVideoId(@PathVariable Integer videoId, HttpServletRequest request) {
        if (request.getAttribute("userId") == null) {
            return Result.error("请先登录");
        }
        Integer userId = (Integer) request.getAttribute("userId");
        log.info("delVideoByVideoId: userId={},videoId={}", userId, videoId);
        try {
            videosService.delVideoByVideoId(userId, videoId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

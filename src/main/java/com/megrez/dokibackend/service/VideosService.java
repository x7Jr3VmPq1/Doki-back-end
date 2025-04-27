package com.megrez.dokibackend.service;

import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.dto.VideoDTO;
import com.megrez.dokibackend.dto.VideoUploadInfoDTO;
import com.megrez.dokibackend.entity.Comment;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.vo.VideoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface VideosService {
    /**
     * 获取所有视频
     *
     * @param userId
     * @return
     */
    List<VideoVO> getAllVideos(Integer userId);

    /**
     * 根据关键字搜索视频
     *
     * @param keyword
     * @param userId
     * @return
     */
    List<VideoVO> searchVideosByKeyword(String keyword, Integer userId);

    /**
     * 获取用户发布的视频
     *
     * @param userId   // 如果登录了，根据用户ID判断视频集合的点赞收藏状态
     * @param userName // 要获取的用户名
     * @return
     */
    List<VideoVO> getVideosByUserName(Integer userId, String userName);

    /**
     * 获取用户喜欢的视频
     *
     * @param userId  // 如果登录了，根据用户ID判断视频集合的点赞收藏状态
     * @param userName // 要获取的用户名
     * @return
     */
    List<VideoVO> getLikeVideosByUserName(Integer userId, String userName);

    /**
     * 获取用户收藏的视频
     *
     * @param userId  // 如果登录了，根据用户ID判断视频集合的点赞收藏状态
     * @param userName // 要获取的用户名
     * @return
     */
    List<VideoVO> getFavoriteVideosByUserName(Integer userId, String userName);
    /**
     * 添加点赞记录
     *
     * @param userId
     * @param videoId
     */
    boolean addlikeRecord(Integer userId, Integer videoId);

    /**
     * 添加收藏记录
     *
     * @param userId
     * @param videoId
     */
    boolean addCollectRecord(Integer userId, Integer videoId);


    /**
     * 上传视频
     *
     * @param video
     * @param userId
     * @return
     */
    boolean uploadVideo(MultipartFile video, Integer userId);

    /**
     * 发布视频
     *
     * @param videoUploadInfoDTO
     * @param userId
     */
    void publishVideo(VideoUploadInfoDTO videoUploadInfoDTO, Integer userId);

    /**
     * 删除视频
     *
     * @param userId
     * @param videoId
     */
    void delVideoByVideoId(Integer userId, Integer videoId);


}

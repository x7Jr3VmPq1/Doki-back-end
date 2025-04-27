package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.entity.Comment;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.entity.VideoTag;
import com.megrez.dokibackend.vo.VideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideosInfoMapper {
    /**
     * 获取所有视频的信息。
     *
     * @return 包含所有视频信息的 Video 对象列表
     */
    List<Video> getAllVideos();

    /**
     * 根据视频 ID 获取视频信息
     *
     * @param keyword // 关键词
     * @return 包含所有视频信息的 Video 对象列表
     */
    List<Video> searchVideosByKeyword(@Param("keyword") String keyword);

    /**
     * 根据用户名获取该用户发布的视频信息
     *
     * @param userName 用户名
     * @return
     */
    List<VideoVO> getVideosByUserName(@Param("userName") String userName);

    /**
     * 根据用户名获取该用户喜欢的视频信息
     * @param userName 用户名
     * @return
     */
    List<VideoVO> getLikeVideosByUserName(@Param("userName") String userName);
    /**
     * 根据用户名获取该用户收藏的视频信息
     * @param userName 用户名
     * @return
     */
    List<VideoVO> getFavoriteVideosByUserName(@Param("userName") String userName);


    /**
     * 判断用户是否已经点赞过该视频
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return
     */
    boolean isLikeRecordExist(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 判断用户是否已经收藏过该视频
     *
     * @param userId
     * @param videoId
     * @return
     */
    boolean isCollectRecordExist(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 添加点赞记录
     *
     * @param userId
     * @param videoId
     */
    void addLikeRecord(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 删除点赞记录
     *
     * @param userId
     * @param videoId
     */
    void delLikeRecord(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 添加收藏记录
     *
     * @param userId
     * @param videoId
     */
    void addCollectRecord(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

    /**
     * 删除收藏记录
     *
     * @param userId
     * @param videoId
     */
    void delCollectRecord(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

}

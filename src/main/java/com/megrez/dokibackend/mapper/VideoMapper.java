package com.megrez.dokibackend.mapper;

import com.megrez.dokibackend.dto.VideoDTO;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.entity.VideoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {
    /**
     * 添加待发布视频
     *
     * @param userId
     * @param videoUrl
     * @param status
     */
    void addPendingVideo(@Param("userId") Integer userId,
                         @Param("videoUrl") String videoUrl,
                         @Param("status") Integer status);

    /**
     * 根据用户id获取待发布视频的url
     *
     * @param userId
     * @return
     */
    String getPendingVideoUrlByUserId(@Param("userId") Integer userId);

    /**
     * 删除待发布视频
     *
     * @param userId
     */
    void delPendingVideoByUserId(@Param("userId") Integer userId);

    /**
     * 根据视频ID获取视频信息
     *
     * @param videoId
     */
    Video getVideoById(@Param("videoId") Integer videoId);

    /**
     * 插入视频信息
     *
     * @param video 视频信息
     */
    void insertVideo(Video video);

    /**
     * 插入视频标签
     *
     * @param videoId 视频ID
     * @param tag     标签
     */
    void insertVideoTag(@Param("videoId") Integer videoId,
                        @Param("tag") String tag);

    /**
     * 根据视频ID删除视频信息
     *
     * @param videoId 视频ID
     */
    void delVideoByVideoId(@Param("videoId") Integer videoId);

    List<String> getVideoTagsByVideoId(Integer id);
}

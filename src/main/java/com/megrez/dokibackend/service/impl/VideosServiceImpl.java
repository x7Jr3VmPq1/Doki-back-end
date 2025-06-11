package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.common.NotificationType;
import com.megrez.dokibackend.config.RabbitConfig;
import com.megrez.dokibackend.controller.WebSocketController;
import com.megrez.dokibackend.dto.CommentsDTO;
import com.megrez.dokibackend.dto.SingleCommentDTO;
import com.megrez.dokibackend.dto.VideoDTO;
import com.megrez.dokibackend.dto.VideoUploadInfoDTO;
import com.megrez.dokibackend.entity.Comment;
import com.megrez.dokibackend.entity.User;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.entity.VideoTag;
import com.megrez.dokibackend.event.Payload;
import com.megrez.dokibackend.mapper.UserMapper;
import com.megrez.dokibackend.mapper.VideoMapper;
import com.megrez.dokibackend.mapper.VideosInfoMapper;
import com.megrez.dokibackend.service.VideosService;
import com.megrez.dokibackend.utils.*;
import com.megrez.dokibackend.vo.VideoVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

/**
 * VideosServiceImpl 类实现了 VideosService 接口，负责处理与视频相关的业务逻辑。
 */
@Service
public class VideosServiceImpl implements VideosService {

    private final VideosInfoMapper videosInfoMapper;
    private final VideoMapper videoMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;

    public VideosServiceImpl(VideosInfoMapper videosInfoMapper,
                             VideoMapper videoMapper,
                             UserMapper userMapper,
                             ApplicationEventPublisher eventPublisher, RabbitTemplate rabbitTemplate) {
        this.videosInfoMapper = videosInfoMapper;
        this.videoMapper = videoMapper;
        this.eventPublisher = eventPublisher;
        this.userMapper = userMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 获取所有视频的信息，并将其转换为 VideoVO 对象列表。
     *
     * @return 包含所有视频信息的 VideoVO 对象列表
     */
    @Override
    public List<VideoVO> getAllVideos(Integer userId) {
        List<VideoVO> videoVOList = new ArrayList<>();
        // 遍历从数据库中获取的所有视频信息，并将其转换为 VideoVO 对象
        for (Video video : videosInfoMapper.getAllVideos()) {
            // 获取视频标签
            VideoVO videoVO = new VideoVO();
            List<String> tags = videoMapper.getVideoTagsByVideoId(video.getId());
            videoVO.setTags(tags);
            // 如果用户ID不为空，则根据用户ID和视频ID查询点赞和收藏信息
            if (userId != null) {
                videoVO.setLiked(videosInfoMapper.isLikeRecordExist(userId, video.getId()));
                videoVO.setFavorited(videosInfoMapper.isCollectRecordExist(userId, video.getId()));
            }
            // 使用 BeanUtils 将 Video 对象的属性复制到 VideoVO 对象中
            BeanUtils.copyProperties(video, videoVO);
            videoVOList.add(videoVO);
        }
        return videoVOList;
    }


    /**
     * 通用接口，根据传入的方法查询视频
     *
     * @param userId        用户ID
     * @param videoSupplier 视频查询方法
     * @return
     */
    private List<VideoVO> getVideosBySupplier(Integer userId, Supplier<List<VideoVO>> videoSupplier) {
        List<VideoVO> videoVOList = videoSupplier.get();
        for (VideoVO video : videoVOList) {
            // 获取视频标签
            List<String> tags = videoMapper.getVideoTagsByVideoId(video.getId());
            video.setTags(tags);
            // 如果用户ID不为空，则根据用户ID和视频ID查询点赞和收藏信息
            if (userId != null) {
                video.setLiked(videosInfoMapper.isLikeRecordExist(userId, video.getId()));
                video.setFavorited(videosInfoMapper.isCollectRecordExist(userId, video.getId()));
            }
        }
        return videoVOList;
    }

    /**
     * 根据用户名获取视频列表。
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @return
     */
    @Override
    public List<VideoVO> getVideosByUserName(Integer userId, String userName) {
        if (userName == null) {
            return List.of();
        }
        return getVideosBySupplier(userId, () -> videosInfoMapper.getVideosByUserName(userName));
    }

    /**
     * 根据用户名获取点赞视频列表。
     *
     * @param userId
     * @param userName
     * @return
     */
    @Override
    public List<VideoVO> getLikeVideosByUserName(Integer userId, String userName) {
        if (userName == null) {
            return List.of();
        }
        return getVideosBySupplier(userId, () -> videosInfoMapper.getLikeVideosByUserName(userName));
    }

    /**
     * 根据用户名获取收藏视频列表。
     *
     * @param userId
     * @param userName
     * @return
     */
    @Override
    public List<VideoVO> getFavoriteVideosByUserName(Integer userId, String userName) {
        if (userName == null) {
            return List.of();
        }
        return getVideosBySupplier(userId, () -> videosInfoMapper.getFavoriteVideosByUserName(userName));
    }

    /**
     * 为给定的用户添加点赞记录。
     *
     * @param userId
     * @param videoId
     */
    @Override
    public boolean addlikeRecord(Integer userId, Integer videoId) {
        // 如果已经存在点赞记录，则删除点赞记录
        if (videosInfoMapper.isLikeRecordExist(userId, videoId)) {
            videosInfoMapper.delLikeRecord(userId, videoId);
            return false;
            // 否则，添加点赞记录
        } else {
            videosInfoMapper.addLikeRecord(userId, videoId);

        }
        Video videoById = videoMapper.getVideoById(videoId);
        // 如果是自己给自己的视频，则不推送任何消息
        if (videoById.getUserId().equals(userId)) {
            return true;
        }
        eventPublisher.publishEvent(new Payload(
                videoById.getUserId(),
                userId,
                NotificationType.LIKE,
                null,
                null,
                videoId));
        return true;
    }

    /**
     * 为给定的用户添加收藏记录。
     *
     * @param userId
     * @param videoId
     */
    @Override
    public boolean addCollectRecord(Integer userId, Integer videoId) {
        // 如果已经存在收藏记录，则删除收藏记录
        if (videosInfoMapper.isCollectRecordExist(userId, videoId)) {
            videosInfoMapper.delCollectRecord(userId, videoId);
            return false;
            // 否则，添加收藏记录
        } else {
            videosInfoMapper.addCollectRecord(userId, videoId);
            return true;
        }
    }

    /**
     * 上传视频文件。
     *
     * @param video
     * @param userId
     * @return
     */
    @Override
    public boolean uploadVideo(MultipartFile video, Integer userId) {
        if (video.isEmpty()) {
            return false; // 文件为空，上传失败
        }
        try {
            // 保存文件
            String videoURL = FileUtils.saveVideo(video);
            if (videoURL == null) {
                return false;
            }
            // 先判断是不是已经存在一条待审核的视频,向videoMapper查询视频URL
            String url = videoMapper.getPendingVideoUrlByUserId(userId);
            if (url != null) {
                // 如果存在，删除这条记录，并把文件服务器中对应的视频删除
                videoMapper.delPendingVideoByUserId(userId);
                FileUtils.deleteVideo(url);
            }
            videoMapper.addPendingVideo(userId, videoURL, 0);
            return true; // 上传成功
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败", e);
        }
    }

    /**
     * 发布视频
     *
     * @param videoUploadInfoDTO 视频上传信息
     * @param userId             用户ID
     */
    @Override
    @Transactional
    public void publishVideo(VideoUploadInfoDTO videoUploadInfoDTO, Integer userId) throws IOException {
        // 从待上传视频表中获取视频URL
        String folderName = videoMapper.getPendingVideoUrlByUserId(userId);
        if (folderName == null) {
            throw new RuntimeException("没有找到待发布的视频");
        }
        // 保存封面图
        String cover;
        if (!Objects.equals(videoUploadInfoDTO.getCover(), "")) {
            // 如果用户上传了封面图，则保存封面图
            cover = FileUtils.saveCover(videoUploadInfoDTO.getCover(), folderName);
        } else {
            // 如果用户没有上传封面图，则使用FFmpeg生成封面图
            cover = FFmpegUtils.createThumbnail(folderName);
        }
        // 向videos表插入数据
        Video video = new Video();
        video.setUserId(userId);
        video.setTitle(videoUploadInfoDTO.getVideoTitle());
        video.setDescription(videoUploadInfoDTO.getVideoDesc());
        video.setVideoUrl(FileServerURL.videoFilesPath + folderName + "/video.mp4");
        video.setThumbnailUrl(cover);
        video.setCategory(videoUploadInfoDTO.getCategory());
        video.setDuration(FFmpegUtils.getVideoDuration(folderName));
        video.setViews(0);
        video.setLikeCount(0);
        video.setCommentCount(0);
        video.setFavoriteCount(0);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        User userById = userMapper.getUserById(userId);
        // 设置用户名和头像
        video.setUserName(userById.getUserName());
        video.setAvatarUrl(userById.getAvatarUrl());
        videoMapper.insertVideo(video);
        // 向video_tags表插入数据
        if (videoUploadInfoDTO.getTags() != null) {
            for (String tag : videoUploadInfoDTO.getTags()) {
                VideoTag videoTag = new VideoTag();
                videoTag.setVideoId(video.getId());
                videoTag.setTag(tag);
                videoMapper.insertVideoTag(
                        videoTag.getVideoId(),
                        videoTag.getTag()
                );
            }
        }
        videoDocument videoDocument = new videoDocument(video, videoUploadInfoDTO.getTags());

        // 发送消息给RabbitMQ,同步数据给搜索引擎
        TransactionCallbackUtil.runAfterCommit(() -> {
            rabbitTemplate.convertAndSend(RabbitConfig.ES_UPDATE_QUEUE, videoDocument);
        });
        // 删除待上传视频表中的记录
        videoMapper.delPendingVideoByUserId(userId);
    }

    /**
     * 删除视频
     *
     * @param userId
     * @param videoId
     */
    @Override
    public void delVideoByVideoId(Integer userId, Integer videoId) {
        // 先根据视频ID查询视频信息。
        // 如果视频的上传用户与当前用户不一致，则抛出异常。
        Video video = videoMapper.getVideoById(videoId);
        if (video == null) {
            throw new RuntimeException("视频不存在");
        } else if (!video.getUserId().equals(userId)) {
            throw new RuntimeException("当前用户不是视频上传者");
        } else {
            // 删除视频信息
            videoMapper.delVideoByVideoId(videoId);
            // 发送消息给RabbitMQ,同步数据给搜索引擎
            TransactionCallbackUtil.runAfterCommit(() -> {
                rabbitTemplate.convertAndSend(RabbitConfig.ES_DELETE_QUEUE, video.getId().toString());
            });
        }
    }
}

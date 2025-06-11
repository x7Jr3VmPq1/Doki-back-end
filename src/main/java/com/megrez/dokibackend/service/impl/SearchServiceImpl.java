package com.megrez.dokibackend.service.impl;

import com.megrez.dokibackend.mapper.SearchMapper;
import com.megrez.dokibackend.mapper.VideoMapper;
import com.megrez.dokibackend.mapper.VideosInfoMapper;
import com.megrez.dokibackend.service.SearchService;
import com.megrez.dokibackend.utils.ElasticsearchUtil;
import com.megrez.dokibackend.utils.RedisUtil;
import com.megrez.dokibackend.utils.videoDocument;
import com.megrez.dokibackend.vo.VideoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    private final VideosInfoMapper videosInfoMapper;
    private final RedisUtil redisUtil;
    private final SearchMapper searchMapper;

    public SearchServiceImpl(VideosInfoMapper videosInfoMapper, RedisUtil redisUtil, SearchMapper searchMapper) {
        this.videosInfoMapper = videosInfoMapper;
        this.redisUtil = redisUtil;
        this.searchMapper = searchMapper;
    }

    /**
     * 根据关键字搜索视频。
     *
     * @param keyword 关键字
     * @param userId  用户ID
     * @return
     */
    @Override
    public List<VideoVO> searchVideosByKeyword(String keyword, Integer userId) throws IOException {
        // 使用搜索引擎进行搜索
        List<Map<String, Object>> result = ElasticsearchUtil.searchDocumentWithHighlight(ElasticsearchUtil.VIDEOS_INDEX, keyword);
        // 增加计数
        redisUtil.recordSearch(keyword);
        // 转换为  VideoVO 对象
        List<VideoVO> videoVOList = new ArrayList<>();
        for (Map<String, Object> document : result) {
            videoDocument videoDocument = ElasticsearchUtil.objectMapper.convertValue(document, videoDocument.class);
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(videoDocument, videoVO);
            // 如果用户不为空，则根据用户ID和视频ID查询点赞和收藏信息
            if (userId != null) {
                videoVO.setLiked(videosInfoMapper.isLikeRecordExist(userId, videoVO.getId()));
                videoVO.setFavorited(videosInfoMapper.isCollectRecordExist(userId, videoVO.getId()));
            }
            videoVOList.add(videoVO);
        }
       /* // 下面的方法暂时不需要 ...
        // 遍历从数据库中获取的所有视频信息，并将其转换为 VideoVO 对象
        for (Video video : videosInfoMapper.searchVideosByKeyword(keyword)) {
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
        }*/
        return videoVOList;
    }

    @Override
    public List<String> getHotSearch() {
        return searchMapper.getHotSearch();
    }

}

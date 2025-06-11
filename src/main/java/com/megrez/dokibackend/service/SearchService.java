package com.megrez.dokibackend.service;

import com.megrez.dokibackend.vo.VideoVO;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    /**
     * 根据关键字搜索视频
     *
     * @param keyword
     * @param userId
     * @return
     */
    List<VideoVO> searchVideosByKeyword(String keyword, Integer userId) throws IOException;

    /**
     * 获取热门搜索
     *
     * @return
     */
    List<String> getHotSearch();
}

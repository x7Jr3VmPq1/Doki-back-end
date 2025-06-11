package com.megrez.dokibackend.controller;

import com.megrez.dokibackend.common.Result;
import com.megrez.dokibackend.service.SearchService;
import com.megrez.dokibackend.vo.VideoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

//    @GetMapping("/videos/search")
//    public Result<List<VideoVO>> searchVideosByKeyword(@RequestParam String keyword, HttpServletRequest request) {
//        Integer userId = (Integer) request.getAttribute("userId");
//        try {
//            List<VideoVO> videoVOS = searchService.searchVideosByKeyword(keyword, userId);
//            return Result.success(videoVOS);
//        } catch (IOException e) {
//            return Result.error("搜索失败");
//        }
//    }

    @GetMapping("/search/hot")
    public Result<List<String>> getHotSearch() {
        List<String> hotSearch = searchService.getHotSearch();
        return Result.success(hotSearch);
    }
}

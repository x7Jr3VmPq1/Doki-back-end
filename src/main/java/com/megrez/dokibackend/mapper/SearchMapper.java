package com.megrez.dokibackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    /**
     * 插入搜索记录
     *
     * @param keyword 搜索关键词
     * @param count   搜索次数
     */
    void recordSearch(@Param("keyword") String keyword,
                      @Param("count") long count);

    /**
     * 获取热门搜索(top10)
     *
     * @return 热门搜索列表
     */
    List<String> getHotSearch();
}

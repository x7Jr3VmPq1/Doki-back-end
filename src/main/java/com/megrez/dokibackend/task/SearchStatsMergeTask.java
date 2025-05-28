package com.megrez.dokibackend.task;

import com.megrez.dokibackend.mapper.SearchMapper;
import com.megrez.dokibackend.utils.RedisConstant;
import com.megrez.dokibackend.utils.RedisUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component

public class SearchStatsMergeTask {
    private final StringRedisTemplate stringRedisTemplate;
    private final SearchMapper searchMapper;

    public SearchStatsMergeTask(StringRedisTemplate stringRedisTemplate, SearchMapper searchMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.searchMapper = searchMapper;
    }

    /**
     * 定时任务，将 Redis 中的搜索统计数据合并到数据库中
     */
    @Scheduled(cron = "0 5 0 * * ?") // 每天 00:05 执行一次
    public void mergeRedisSearchStatsToDb() {
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        String redisKey = RedisConstant.SEARCH_COUNT_KEY + yesterday;

        Map<Object, Object> keywordCounts = stringRedisTemplate.opsForHash().entries(redisKey);

        if (keywordCounts.isEmpty()) {
            return;
        }

        for (Map.Entry<Object, Object> entry : keywordCounts.entrySet()) {
            String keyword = (String) entry.getKey();
            long count = Long.parseLong(entry.getValue().toString());
            searchMapper.recordSearch(keyword, count);
        }

        // 清理 Redis 数据，防止堆积
        stringRedisTemplate.delete(redisKey);
    }
}

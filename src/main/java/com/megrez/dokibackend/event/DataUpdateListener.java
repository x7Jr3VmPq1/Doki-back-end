package com.megrez.dokibackend.event;

import com.megrez.dokibackend.config.RabbitConfig;
import com.megrez.dokibackend.utils.ElasticsearchUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.megrez.dokibackend.utils.videoDocument;

import java.io.IOException;

@Component
public class DataUpdateListener {
    @RabbitListener(queues = RabbitConfig.ES_UPDATE_QUEUE)
    public void handleESUpdate(videoDocument videoDocument) throws IOException {
        // 写入videoDocument到搜索引擎
        ElasticsearchUtil.insertDocument(ElasticsearchUtil.VIDEOS_INDEX,
                videoDocument.getId().toString(),
                videoDocument);
    }

    @RabbitListener(queues = RabbitConfig.ES_DELETE_QUEUE)
    public void handleESDelete(String videoId) throws IOException {
        // 删除videoDocument在搜索引擎中的记录
        ElasticsearchUtil.deleteDocument(ElasticsearchUtil.VIDEOS_INDEX, videoId);
    }

    @RabbitListener(queues = RabbitConfig.REDIS_QUEUE)
    public void handleRedisUpdate(String payload) {
        System.out.println("Redis Received payload: " + payload);
        // 处理数据更新逻辑
    }
}

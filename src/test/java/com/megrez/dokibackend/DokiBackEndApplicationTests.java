package com.megrez.dokibackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.megrez.dokibackend.common.FileServerURL;
import com.megrez.dokibackend.config.RabbitConfig;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.service.VideosService;
import com.megrez.dokibackend.task.SearchStatsMergeTask;
import com.megrez.dokibackend.utils.*;
import com.megrez.dokibackend.vo.VideoVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class DokiBackEndApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VideosService videosService;

    @Autowired
    SearchStatsMergeTask searchStatsMergeTask;

    @Test
    void contextLoads() throws IOException {
        searchStatsMergeTask.mergeRedisSearchStatsToDb();
    }

    @Test
    void testGetDocument() throws IOException {
        Map<String, Object> document = ElasticsearchUtil.getDocument(ElasticsearchUtil.VIDEOS_INDEX, "74");
        videoDocument videoDocument = ElasticsearchUtil.objectMapper.convertValue(document, videoDocument.class);

        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(videoDocument, videoVO);
        System.out.println(videoVO);

    }

    @Test
    void testSearch() throws IOException {
        List<Map<String, Object>> documents = ElasticsearchUtil.searchDocument(ElasticsearchUtil.VIDEOS_INDEX, "老虎动画");
        for (Map<String, Object> document : documents) {
            videoDocument videoDocument = ElasticsearchUtil.objectMapper.convertValue(document, videoDocument.class);
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(videoDocument, videoVO);
            System.out.println(videoVO);
        }
    }

    @Test
    void testSearchWithHighlight() throws IOException {
        List<Map<String, Object>> documents = ElasticsearchUtil.searchDocumentWithHighlight(ElasticsearchUtil.VIDEOS_INDEX, "老虎动画");
        for (Map<String, Object> document : documents) {
            videoDocument videoDocument = ElasticsearchUtil.objectMapper.convertValue(document, videoDocument.class);
            VideoVO videoVO = new VideoVO();
            BeanUtils.copyProperties(videoDocument, videoVO);
            System.out.println(videoVO);
        }
    }

    @Test
    void testRabbitMQ() throws InterruptedException {
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("消息发送失败：" + cause);
            } else {
                System.out.println("消息成功发送到交换机");
            }
        });

        rabbitTemplate.convertAndSend(RabbitConfig.ES_UPDATE_QUEUE, "Hello 现在需要更新数据~!");

        Thread.sleep(3000); // 等待 3 秒，给回调时间
    }

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void testRedis() {
        redisUtil.set(RedisConstant.VIDEO_KEY + "2:" + RedisConstant.LIKE_KEY, "100");
        redisUtil.set(RedisConstant.VIDEO_KEY + "2:" + RedisConstant.COLLECT_KEY, "21");
    }

    @Test
    void testFileMove() throws IOException {
        int videoDuration = FFmpegUtils.getVideoDuration("db3a8f8c-6a4f-4573-8c5d-f79f28beee22");
        System.out.println(videoDuration);
    }
}

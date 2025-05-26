package com.megrez.dokibackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.megrez.dokibackend.entity.Video;
import com.megrez.dokibackend.service.VideosService;
import com.megrez.dokibackend.utils.ElasticsearchUtil;
import com.megrez.dokibackend.utils.JWTUtil;
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

    @Test
    void contextLoads() throws IOException {

        List<VideoVO> allVideos = videosService.getAllVideos(10014);
        for (VideoVO video : allVideos) {
            videoDocument videoDocument = new videoDocument(video);
            ElasticsearchUtil.insertDocument(ElasticsearchUtil.VIDEOS_INDEX, video.getId().toString(), videoDocument);
        }
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
}

package com.megrez.dokibackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElasticsearchUtil {

    // 创建客户端
    private static final RestHighLevelClient client;
    // JSON转换器
    public static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.88.130", 9200, "http")
                )
        );
    }

    // 索引库名称
    public static final String VIDEOS_INDEX = "videos";

    // 插入/更新文档
    public static <T> String insertDocument(String index, String id, T document) throws IOException {
        String json = objectMapper.writeValueAsString(document);
        IndexRequest request = new IndexRequest(index).id(id).source(json, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        return response.getId();
    }

    // 获取文档
    public static Map<String, Object> getDocument(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    // 删除文档
    public static void deleteDocument(String index, String id) throws IOException {
        client.delete(new DeleteRequest(index, id), RequestOptions.DEFAULT);
    }

    // 根据关键词搜索
    public static List<Map<String, Object>> searchDocument(String index, String keyword) throws IOException {
        // 1.准备Request
        SearchRequest request = new SearchRequest(index);
        // 2.准备DSL
        request.source()
                .query(QueryBuilders.matchQuery("title", keyword));
        // 3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            result.add(sourceAsMap);
        }
        return result;
    }

    // 关闭客户端（在应用关闭时调用）
    public static void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}

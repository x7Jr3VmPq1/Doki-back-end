package com.megrez.dokibackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "db_update_exchange";
    public static final String ES_INSERT_QUEUE = "es_insert_queue";
    public static final String ES_UPDATE_QUEUE = "es_update_queue";
    public static final String ES_DELETE_QUEUE = "es_delete_queue";
    public static final String REDIS_QUEUE = "redis_update_queue";

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public FanoutExchange dbUpdateExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue esUpdateQueue() {
        return new Queue(ES_UPDATE_QUEUE, true);
    }

    @Bean
    public Queue esInsertQueue() {
        return new Queue(ES_INSERT_QUEUE, true);
    }

    @Bean
    public Queue esDeleteQueue() {
        return new Queue(ES_DELETE_QUEUE, true);
    }

    @Bean
    public Queue redisUpdateQueue() {
        return new Queue(REDIS_QUEUE, true);
    }

    // 队列绑定到交换机
    @Bean
    public Binding bindingEs(FanoutExchange dbUpdateExchange, Queue esUpdateQueue) {
        return BindingBuilder.bind(esUpdateQueue).to(dbUpdateExchange);
    }

    @Bean
    public Binding bindingRedis(FanoutExchange dbUpdateExchange, Queue redisUpdateQueue) {
        return BindingBuilder.bind(redisUpdateQueue).to(dbUpdateExchange);
    }
}

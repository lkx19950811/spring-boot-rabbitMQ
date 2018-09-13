package com.lkx.rabbitdemo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * topic 支持通配符模式
 * @author leon
 * @since 2018-03-27 14:35
 */
@Component
@RabbitListener(queues = "topic.message")
public class TopicMessageReceriver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(String msg){
        logger.warn("topicMessageReceiver  :{}",msg);
    }

}
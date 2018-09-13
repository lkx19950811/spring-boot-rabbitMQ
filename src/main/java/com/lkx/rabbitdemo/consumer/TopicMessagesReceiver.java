package com.lkx.rabbitdemo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  此处绑定的位 topic.# 通配 topic key下的所有消息
 * @author lee
 * @since 2018-03-27 14:38
 */
@Component
@RabbitListener(queues = "topic.messages")
public class TopicMessagesReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(String msg){
        logger.warn("topicMessagesReceiver  :{}",msg);
    }

}
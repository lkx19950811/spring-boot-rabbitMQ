package com.lkx.rabbitdemo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author young
 * @since 2018-03-27 14:31
 */
@Component
public class TopicSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TopicSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(){
        String msg1 = "I am topic.message msg======";
        logger.warn("sender1 : {}",msg1);
        this.rabbitTemplate.convertAndSend("exchange","topic.message",msg1);//topic.message初始化的时候绑定的路由key为 topic.message

        String msg2 = "I am topic.messages msg######";
        logger.warn("sender2 : {}",msg2);
        this.rabbitTemplate.convertAndSend("exchange","topic.messages",msg2);//topic.messages初始化的时候绑定的路由key为 topic.#
    }
}
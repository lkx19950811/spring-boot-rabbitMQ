package com.lkx.rabbitdemo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 生产者2
 *
 * @author leo
 * @since 2018-03-23 9:25
 */
@Component
public class HelloSender2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void send(String msg) {
        String sendMsg = msg + new Date();
        logger.warn("HelloSender2 :{}",sendMsg);
        this.rabbitTemplate.convertAndSend("hello", sendMsg);
    }
}
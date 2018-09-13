package com.lkx.rabbitdemo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者1
 *  最简单的hello world
 * @author leo
 * @since 2018-03-21 15:15
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(String message) {
        logger.warn("helloReceiver1 :{}",message);
    }

}
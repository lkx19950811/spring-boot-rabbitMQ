package com.lkx.rabbitdemo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 最简单的work 竞争模式的消费者
 * @author lee Cather
 * @date 2018-09-10 17:53
 * desc : 消费者2
 */
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver2 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(String message){
        logger.warn("helloReceiver2 :{}",message);
    }
}

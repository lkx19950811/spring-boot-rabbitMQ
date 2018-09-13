package com.lkx.rabbitdemo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author leo
 * @since 2018-03-28 10:31
 */

/**
 * 绑定在 fanoutExchange 也就是俗称的广播模式
 */
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(String msg) {
        logger.warn("FanoutReceiverA  : " + msg);
    }

}
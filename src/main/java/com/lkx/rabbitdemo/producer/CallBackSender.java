package com.lkx.rabbitdemo.producer;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallBackSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send() {
        String msg="callbackSender : i am callback sender";
        logger.warn(msg);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());  
        logger.warn("callbackSender UUID: {}",correlationData.getId());
        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", msg, correlationData);
    }
}
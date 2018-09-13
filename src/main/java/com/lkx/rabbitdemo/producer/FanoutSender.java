package com.lkx.rabbitdemo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author leo
 * @since 2018-03-27 15:00
 */
@Component
public class FanoutSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public FanoutSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(){
        String msgString="fanoutSender :hello i am lkx";
        logger.warn(msgString);
        this.rabbitTemplate.convertAndSend("fanoutExchange","abcd.ee", msgString);
    }
    public void sendListen(){
        String msgString="listen :hello listen i am lkx";
        logger.warn(msgString);
        this.rabbitTemplate.convertAndSend("listen", msgString);
    }
    public void sendFanoutListen(){
        String msgString="listen on mehtod :hello listen i am lkx";
        logger.warn(msgString);
        this.rabbitTemplate.convertAndSend("FANOUT_EXCHANGE","abc", msgString);
    }
}
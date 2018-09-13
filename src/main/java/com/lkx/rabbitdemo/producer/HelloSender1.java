package com.lkx.rabbitdemo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lee Cather
 * @date 2018-09-10 17:14
 * desc : hello生产者1
 */
@Component
public class HelloSender1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(){
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String sendMsg = "hello " + sf.format(new Date());
        logger.warn("send1: {}",sendMsg);
        rabbitTemplate.convertAndSend("hello",sendMsg);
    }
    public void send(String message){
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String sendMsg = message + sf.format(new Date());
        logger.warn("send1: {}",sendMsg);
        rabbitTemplate.convertAndSend("hello",sendMsg);
    }
}

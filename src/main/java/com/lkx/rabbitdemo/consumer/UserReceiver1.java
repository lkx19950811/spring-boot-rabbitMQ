package com.lkx.rabbitdemo.consumer;

import com.alibaba.fastjson.JSONObject;
import com.lkx.rabbitdemo.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 测试pojo传输 没有使用jackson,后续会使用jackson
 * @author lee Cather
 * @date 2018-09-11 10:02
 * desc :
 */
@Component
@RabbitListener(queues = "user")
public class UserReceiver1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitHandler
    public void process(User user){
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        logger.warn("userReceive1: {}---{}",user,sf.format(new Date()));

    }

}

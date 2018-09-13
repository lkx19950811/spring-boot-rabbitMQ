package com.lkx.rabbitdemo.producer;

import com.lkx.rabbitdemo.RandomMock;
import com.lkx.rabbitdemo.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author lee Cather
 * @date 2018-09-10 17:50
 * desc :
 */
@Component
public class UserSend1 {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserSend1(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(){
        User user = new User();
        user.setFullName(RandomMock.makeName());
        user.setUserName(RandomMock.maekEmail());
        user.setPassword(UUID.randomUUID().toString());
        logger.info("usersend1 :{}",user);
        rabbitTemplate.convertAndSend("user",user);
    }
}

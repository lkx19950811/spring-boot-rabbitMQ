package com.lkx.rabbitdemo.controller;

import com.lkx.rabbitdemo.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lee Cather
 * @date 2018-09-10 17:19
 * desc :
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    private final HelloSender1 helloSender1;
    private final HelloSender2 helloSender2;
    private final UserSend1 userSend1;
    private final TopicSender topicSender;
    private final FanoutSender fanoutSender;
    private final CallBackSender callBackSender;
    @Autowired
    public RabbitController(HelloSender1 helloSender1, HelloSender2 helloSender2, UserSend1 userSend1, TopicSender topicSender, FanoutSender fanoutSender, CallBackSender callBackSender) {
        this.helloSender1 = helloSender1;
        this.helloSender2 = helloSender2;
        this.userSend1 = userSend1;
        this.topicSender = topicSender;
        this.fanoutSender = fanoutSender;
        this.callBackSender = callBackSender;
    }

    @RequestMapping("/hello")
    public void hello(){
        helloSender1.send();
    }
    @RequestMapping("/oneToM")
    public void oneToM(){
        for (int i=0;i<10;i++){
            helloSender1.send();
        }
    }
    @RequestMapping("/mTOm")
    public void mTOm(){
        for (int i=0;i<10;i++){
            helloSender1.send("helloMsg " + i + "----");
            helloSender2.send("helloMsg " + i + "----");
        }
    }
    @RequestMapping("/pojo")
    public void pojo(){
        userSend1.send();
    }
    /**
     * topic exchange类型rabbitmq测试
     */
    @RequestMapping("/topicTest")
    public void topicTest(){
        topicSender.send();
    }
    /**
     * fanout exchange类型rabbitmq测试
     */
    @GetMapping("/fanoutTest")
    public void fanoutTest() {
        fanoutSender.send();
    }
    @RequestMapping("/callback")
    public void callbak() {
        callBackSender.send();
    }

    /**
     * 测试监听类
     */
    @RequestMapping("/listen")
    public void listen(){
        fanoutSender.sendListen();
    }
    /**
     * 测试直接使用on方法消费
     */
    @RequestMapping("/onFanout")
    public void listenFanout(){
        fanoutSender.sendFanoutListen();
    }
}

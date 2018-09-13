package com.lkx.rabbitdemo.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lee Cather
 * @date 2018-09-11 17:32
 * desc : 使用监听模式
 */
@Component
public class ListenerReceiver {
    enum Action {
        ACCEPT,  // 处理成功
        RETRY,   // 可以重试的错误
        REJECT,  // 无需重试的错误
    }
    private static final Logger logger = LoggerFactory.getLogger(ListenerReceiver.class);

    @RabbitListener(queues = {"fanout.t1"})
    public void lisetnMessageT1(Message message, Channel channel) throws IOException {
    Action action = Action.ACCEPT;
        long tag = 0;
        try {
            tag = message.getMessageProperties().getDeliveryTag();
            //得到了message实体 进行业务处理,可以根据 routingKey 以及 consumerQueue来分发业务
//            logger.warn(message.getMessageProperties().toString());
            logger.warn("FANOUT_QUEUE_T1 :{}",new String(message.getBody(),"UTF-8"));
            // 下面注释每次只接收一个消息
            channel.basicQos(1);
        } catch (Exception e) {
            // 根据异常种类决定是ACCEPT、RETRY还是 REJECT
            action = Action.RETRY;
            e.printStackTrace();
        } finally {
            try {
                // 通过finally块来保证Ack/Nack会且只会执行一次
                if (action.equals(Action.ACCEPT)) {
                    channel.basicAck(tag, true);
                    // 重试
                } else if (action.equals(Action.RETRY) ) {
                    channel.basicNack(tag, false, true);
                    Thread.sleep(2000L);
                    // 拒绝消息也相当于主动删除mq队列的消息
                } else {
                    channel.basicNack(tag, false, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @RabbitListener(queues = {"fanout.t2"})
    public void lisetnMessageT2(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        logger.warn("FANOUT_QUEUE_T2 :"+new String(message.getBody()));
    }
    @RabbitListener(queues = {"listen"})
    public void lisetn(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        logger.warn("FANOUT_QUEUE_listen "+new String(message.getBody()));
    }
}

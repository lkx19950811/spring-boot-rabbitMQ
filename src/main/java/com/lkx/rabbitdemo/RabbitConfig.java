package com.lkx.rabbitdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.rabbitmq.host}")
    private String addresses;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    /**
     * 连接rabbitmq
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses + ":" + port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        /*
          对于每一个RabbitTemplate只支持一个ReturnCallback。
          对于返回消息，模板的mandatory属性必须被设定为true，
          它同样要求CachingConnectionFactory的publisherReturns属性被设定为true。
          如果客户端通过调用setReturnCallback(ReturnCallback callback)注册了RabbitTemplate.ReturnCallback，那么返回将被发送到客户端。
          这个回调函数必须实现下列方法：
         void returnedMessage(Message message, intreplyCode, String replyText,String exchange, String routingKey);
         */
        /*
          同样一个RabbitTemplate只支持一个ConfirmCallback。
          对于发布确认，template要求CachingConnectionFactory的publisherConfirms属性设置为true。
          如果客户端通过setConfirmCallback(ConfirmCallback callback)注册了RabbitTemplate.ConfirmCallback，那么确认消息将被发送到客户端。
          这个回调函数必须实现以下方法：
          void confirm(CorrelationData correlationData, booleanack);
         */
        connectionFactory.setPublisherConfirms(publisherConfirms);
        return connectionFactory;
    }

    /**
     * rabbitAdmin代理类
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }
    /**
     * 创建rabbitTemplate 消息模板类
     * prototype原型模式:每次获取Bean的时候会有一个新的实例
     *  因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(true);//返回消息必须设置为true
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());//数据转换为json存入消息队列 不知道为什么我这里有点问题,先不用了
        //  rabbitTemplate.setReplyAddress(replyQueue().getName());
        //  rabbitTemplate.setReplyTimeout(100000000);
        //发布确认
        /*confirmcallback用来确认消息是否有送达消息队列*/
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData!=null){
                logger.warn("回调: {}",correlationData.getId());
                if (!ack){
                    //TODO resend msg
                    //先在内存型或者关系型数据库存下记录,如果发送失败,则重发
                    logger.debug("发送到queue失败");
                    throw new RuntimeException("send error " + cause);
                }else {
                    //TODO delete in db
                    logger.warn("已完成: {}",correlationData.getId());
                }
            }

        });
        /*若消息找不到对应的Exchange会先触发returncallback */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("send message failed: " + replyCode + " " + replyText);
            rabbitTemplate.send(message);
        });
        return rabbitTemplate;
    }
//    //设置消息监听
//    @Bean
//    public SimpleMessageListenerContainer messageContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(listenQueue());
//        container.setExposeListenerChannel(true);
//        container.setMaxConcurrentConsumers(1);
//        container.setConcurrentConsumers(1);
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//消息确认后才能删除
//        container.setPrefetchCount(5);//每次处理5条消息
//        container.setMessageListener(new QueueListenter());
//        return container;
//    }
    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }
    @Bean
    public Queue listenQueue() {
        return new Queue("listen");
    }
    @Bean
    public Queue fanoutT1(){
        return new Queue("fanout.t1");
    }
    @Bean
    public Queue fanoutT2(){
        return new Queue("fanout.t2");
    }
    /**
     * ===============以下是验证topic Exchange的队列==========
     */
    @Bean
    public Queue queueMessage() {
        return new Queue("topic.message");
    }

    /**
     * ===============以下是验证topic Exchange的队列==========
     */
    @Bean
    public Queue queueMessages() {
        return new Queue("topic.messages");
    }
    //


    /**
     * ===============以下是验证Fanout Exchange的队列==========
     */
    @Bean
    public Queue AMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue BMessage() {
        return new Queue("fanout.B");
    }

    /**
     * ===============以下是验证Fanout Exchange的队列==========
     */
    @Bean
    public Queue CMessage() {
        return new Queue("fanout.C");
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }
    /**
     * 声明 fanout 交换机.
     *
     * @return the exchange
     */
    @Bean("fanoutExchangeNew")
    public FanoutExchange fanoutExchangeNew() {
        return new FanoutExchange("FANOUT_EXCHANGE");
    }
    /**
     * 将队列topic.message与exchange绑定，binding_key为topic.message,就是完全匹配
     * @param queueMessage
     * @param exchange 交换机
     * @return
     */
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    /**
     * 将队列topic.messages与exchange绑定，binding_key为topic.#,模糊匹配
     * @param queueMessages
     * @param exchange 交换机
     * @return
     */
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(BMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(CMessage).to(fanoutExchange);
    }
    @Bean
    Binding bindingExchangeT1(Queue fanoutT1, @Qualifier("fanoutExchangeNew") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutT1).to(fanoutExchange);
    }
    @Bean
    Binding bindingExchangeT2(Queue fanoutT2, @Qualifier("fanoutExchangeNew") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutT2).to(fanoutExchange);
    }
}
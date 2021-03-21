package com.itayventura;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverConfiguration {

    @Value("${amqp.queue.name}")
    private String queueName;

    @Bean
    public Queue queue1(){
        return new Queue(queueName+"1", false);
    }

    @Bean
    public DriverWorker driverWorker1(){
        return new DriverWorker(new Driver("Moshe", "Cohen", "Moshe Dayan, 2, Tel Aviv"));
    }

    @Bean
    public DriverWorker driverWorker2(){
        return new DriverWorker(new Driver("David", "Levi", "Arlozarov, 68, Tel Aviv"));
    }



    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("fanout");
    }

    @Bean
    public Binding binding1(FanoutExchange fanout,
                            Queue queue1) {
        return BindingBuilder.bind(queue1).to(fanout);
    }

    // this is relevant if we want that the message will go to all drivers
//    @Bean
//    public Queue queue2(){
//        return new Queue(queueName+"2", false);
//    }
// this is relevant if we want that the message will go to all drivers
//    @Bean
//    public Binding binding2(FanoutExchange fanout,
//                            Queue queue2) {
//        return BindingBuilder.bind(queue2).to(fanout);
//    }


}


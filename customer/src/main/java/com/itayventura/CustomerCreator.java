package com.itayventura;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@EnableScheduling
public class CustomerCreator implements CommandLineRunner {

    private static int cnt;

    @Value("${amqp.queue.name}")
    private String queueName;



    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCreator.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final FanoutExchange fanout;

    @Autowired
    public CustomerCreator(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, FanoutExchange fanout){
        super();
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.fanout = fanout;
        cnt = 0;
    }

    @Scheduled(fixedRate = 2000L)
    public void sendFanout(){
        try {
            Customer customer = new Customer("Scheduled", "Fanout " + cnt++, "some address");
            String jsonString = objectMapper.writeValueAsString(customer);
            rabbitTemplate.convertAndSend(fanout.getName(), "", jsonString);
            LOGGER.info("[x] " + customer.getFirstName() + " " +customer.getLastName() + " is looking for taxi at: " + customer.getAddress());
        } catch (JsonProcessingException e) {
            LOGGER.error("Parsing Exception", e);
        }
    }

//    @Scheduled(fixedRate = 2000L)
//    public void sendMessage(){
//        try {
//            String jsonString = objectMapper.writeValueAsString(new Customer("Scheduled", "Person", "some address"));
//            rabbitTemplate.convertAndSend(queueName, jsonString);
//            LOGGER.info(" [x] Sent " + jsonString);
//        } catch (JsonProcessingException e) {
//            LOGGER.error("Parsing Exception", e);
//        }
//    }


    @Override
    public void run(String... args) {
//        List<Customer> persons = new ArrayList<>(
//                Arrays.asList(
//                        new Customer("first", "person", "some address")
//                ));
//        persons.forEach(person->{
//            try {
//                String jsonString = objectMapper.writeValueAsString(person);
//                rabbitTemplate.convertAndSend(queueName, jsonString);
//                LOGGER.info(" [x] Sent " + jsonString);
//            } catch (JsonProcessingException e) {
//                LOGGER.error("Parsing Exception", e);
//            }
//        });
    }
}

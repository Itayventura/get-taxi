package com.itayventura;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${amqp.queue.name}")
    private String queueName;

    @Value("$amqp.exchange.name")
    private String exchangeName;


    public CustomerController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

        @PostMapping("/send")
        public String sendPerson(@RequestBody Customer customer){
            try {
                String jsonString = objectMapper.writeValueAsString(customer);
                rabbitTemplate.convertAndSend(queueName, jsonString);
                LOGGER.info(" [x] Sent " + jsonString);
                return "We have sent a message! :" + jsonString;
            } catch (JsonProcessingException e) {
                LOGGER.error("Parsing Exception", e);
                return "we haven't sent a message";
            }
        }

}

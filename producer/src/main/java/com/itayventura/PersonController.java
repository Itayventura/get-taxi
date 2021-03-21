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
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${amqp.queue.name}")
    private String queueName;

    @Value("$amqp.exchange.name")
    private String exchangeName;


    public PersonController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

        @PostMapping("/send")
        public String sendPerson(@RequestBody Person person){
            try {
                String jsonString = objectMapper.writeValueAsString(person);
                rabbitTemplate.convertAndSend(queueName, jsonString);
                LOGGER.info(" [x] Sent " + jsonString);
                return "We have sent a message! :" + jsonString;
            } catch (JsonProcessingException e) {
                LOGGER.error("Parsing Exception", e);
                return "we haven't sent a message";
            }
        }

}

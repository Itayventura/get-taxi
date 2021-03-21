package com.itayventura;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public Processor(ObjectMapper objectMapper){
        super();
        this.objectMapper = objectMapper;
        LOGGER.info(" [*] Waiting for messages. To exit press CTRL+C");
    }

    public void receiveMessage(String personJson){
        LOGGER.info("[x]  Received  " + personJson);
        try{
            Person person = objectMapper.readValue(personJson, Person.class);
            LOGGER.info("[x] " + person + " is ready for processing");
            doWork(person);
            LOGGER.info("[x] " + person + " processed");
        } catch (Exception e){
            LOGGER.error("ex caught", e);
        }
    }

    private void doWork(Person person) throws InterruptedException {
        Thread.sleep(1000);
    }

}

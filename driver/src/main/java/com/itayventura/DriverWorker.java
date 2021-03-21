package com.itayventura;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "#{queue1.name}")
public class DriverWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverWorker.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Driver driver;

    @Autowired
    public DriverWorker(Driver driver){
        super();
        this.driver = driver;
        LOGGER.info(" [*] Waiting for messages. To exit press CTRL+C");
    }

    @RabbitHandler
    public void receiveMessage(String personJson){
        LOGGER.info("[x] driver " + driver.getFirstName() + " " + driver.getLastName() + " at " + driver.getAddress() + " Received  " + personJson);
        try{
            Person person = objectMapper.readValue(personJson, Person.class);
            LOGGER.info("[x] driver " + driver.getFirstName() + " " + driver.getLastName() + " at " + driver.getAddress() + " is driving to " + person);
            doWork(person);
            LOGGER.info("[x] driver " + driver.getFirstName() + " " + driver.getLastName() + " has reached at " + person.getAddress() +  "and picked up " + person.getFirstName() + " " + person.getLastName());
        } catch (Exception e){
            LOGGER.error("ex caught", e);
        }
    }

    private void doWork(Person person) throws InterruptedException {
        Thread.sleep(3000);
    }

}

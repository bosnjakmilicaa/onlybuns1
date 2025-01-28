package com.project.onlybuns.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onlybuns.config.RabbitMQConfig;
import com.project.onlybuns.repository.CareLocationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitCareMessageListener {
    private final CareLocationRepository repository;

    public void CareLocationRepository(CareLocationRepository repository) {

    }

    public RabbitCareMessageListener(CareLocationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)  // Ensure it's listening to the correct queue
    public void receiveMessage(String message) {
        System.out.println("Primljena poruka: " + message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            RabbitCareLocation location = mapper.readValue(message, RabbitCareLocation.class);
            saveLocation(location);
        } catch (Exception e) {
            System.err.println("Greška pri obradi poruke: " + e.getMessage());
        }
    }


    private void saveLocation(RabbitCareLocation location) {
        RabbitCareLocation savedLocation = repository.save(location);

        System.out.println("Lokacija sačuvana: " + location);
    }
}

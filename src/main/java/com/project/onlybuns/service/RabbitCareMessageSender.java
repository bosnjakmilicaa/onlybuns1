package com.project.onlybuns.service;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.onlybuns.config.RabbitMQConfig;
import com.project.onlybuns.model.RabbitCareLocation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitCareMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendLocationMessage(RabbitCareLocation location) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(location);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message); // Send to exchange with routing key
            System.out.println("Poruka poslana: " + message);
        } catch (Exception e) {
            System.err.println("Greška pri slanju poruke: " + e.getMessage());
        }
    }

}
*/
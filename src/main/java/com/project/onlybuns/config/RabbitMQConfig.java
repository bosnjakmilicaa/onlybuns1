package com.project.onlybuns.config;
/*
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "rabbitCareQueue";
    public static final String EXCHANGE_NAME = "rabbitCareExchange";
    public static final String ROUTING_KEY = "rabbitCareRoutingKey";

    @Bean
    public Queue rabbitCareQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public DirectExchange rabbitCareExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue rabbitCareQueue, DirectExchange rabbitCareExchange) {
        Binding binding = BindingBuilder.bind(rabbitCareQueue)
                .to(rabbitCareExchange)
                .with(ROUTING_KEY);
        System.out.println("Binding created between queue: " + rabbitCareQueue.getName() +
                " and exchange: " + rabbitCareExchange.getName() +
                " with routing key: " + ROUTING_KEY);
        return binding;
    }

}*/


package com.project.onlybuns.service;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {

    private final List<String> instances = List.of(
            "http://localhost:8082", // Prva instanca
            "http://localhost:8083"  // Druga instanca
    );
    private final AtomicInteger counter = new AtomicInteger(0);
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> forwardRequest() {
        int retryCount = 0;
        int maxRetries = instances.size();

        while (retryCount < maxRetries) {
            String instance = getNextInstance();
            try {
                Long id = 123L;  // Primer ID-a koji se koristi
                return restTemplate.getForEntity(instance + "/some-endpoint/" + id, String.class);
            } catch (Exception e) {
                retryCount++;
                System.out.println("Instance " + instance + " failed. Retrying...");
            }
        }

        return ResponseEntity.status(503).body("All instances are unavailable.");
    }


    private String getNextInstance() {
        int index = counter.getAndIncrement() % instances.size();
        return instances.get(index);
    }
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 sekundi
        factory.setReadTimeout(5000);    // 5 sekundi
        return new RestTemplate(factory);
    }

}

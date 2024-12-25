package com.project.onlybuns.service;

import com.project.onlybuns.model.RabbitCareLocation;
import com.project.onlybuns.repository.CareLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CareLocationService {

    @Autowired
    private RabbitCareMessageSender messageSender;
    private final CareLocationRepository repository;



    public CareLocationService(CareLocationRepository repository) {
        this.repository = repository;
    }

    public void sendCareLocation() {
        RabbitCareLocation location = new RabbitCareLocation("Veterinarski centar", 45.2671, 19.8335);
        messageSender.sendLocationMessage(location);
    }
    public List<RabbitCareLocation> getAllLocations() {
        return repository.findAll();
    }
}

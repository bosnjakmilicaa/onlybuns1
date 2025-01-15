package com.project.onlybuns.controller;
/*
import com.project.onlybuns.model.RabbitCareLocation;
import com.project.onlybuns.service.AdminUserService;
import com.project.onlybuns.service.CareLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CareLocationController {

    @Autowired
    private CareLocationService careLocationService;
    @Autowired
    public CareLocationController(CareLocationService careLocationService) {
        this.careLocationService = careLocationService;
    }


    @GetMapping("/send-care-location")
    public String sendCareLocation() {
        careLocationService.sendCareLocation();  // Pozivanje metode kad se pristupi ovom URL-u
        return "Poruka poslana!";
    }

    @GetMapping("/care-location")
    public List<RabbitCareLocation> getAllLocations() {
        return careLocationService.getAllLocations();
    }
}
*/
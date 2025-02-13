package com.project.onlybuns.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class WelcomeController {

    @GetMapping("/")  // API ruta
    public String getWelcomeMessage() {
        return "Get Hoppin' With Your Friends";
    }
}

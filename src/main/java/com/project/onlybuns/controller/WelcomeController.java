package com.project.onlybuns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")  // API ruta
    public String getWelcomeMessage() {
        return "WELCOME TO ONLYBUNS APPLICATION";
    }
}

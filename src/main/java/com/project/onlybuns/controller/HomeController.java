package com.project.onlybuns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // Endpoint za početnu stranicu
    @GetMapping("/")
    public String home() {
        return "Welcome"; // Poruka dobrodošlice
    }
}

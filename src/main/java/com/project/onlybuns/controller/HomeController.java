package com.project.onlybuns.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    // Endpoint za početnu stranicu
    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("title", "Dobrodošli");
        response.put("message", "Ovo je vaša aplikacija!");

        return response; // Vraća JSON objekat
    }
}

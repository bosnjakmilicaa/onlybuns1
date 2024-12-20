package com.project.onlybuns.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    @GetMapping("/some-endpoint/{id}")
    public ResponseEntity<String> getExample(@PathVariable Long id) {
        return ResponseEntity.ok("Received ID: " + id);
    }
}

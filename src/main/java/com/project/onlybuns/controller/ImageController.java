package com.project.onlybuns.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping("/example")
    public ResponseEntity<byte[]> getImage() {
        byte[] imageContent = loadImage(); // Učitavanje slike iz baze ili fajl sistema
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "public, max-age=31556926");

        // Logovanje
        logger.info("Successful cache: Image served with Cache-Control header.");

        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    private byte[] loadImage() {
        // Učitavanje slike
        return new byte[0]; // Primer
    }
}

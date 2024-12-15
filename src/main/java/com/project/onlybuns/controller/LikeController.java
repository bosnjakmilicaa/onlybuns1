package com.project.onlybuns.controller;

import com.project.onlybuns.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @GetMapping("/top10UsersLast7Days")
    public ResponseEntity<List<Map<String, Object>>> getTop10UsersByLikesInLast7Days() {
        List<Object[]> results = likeService.getTop10UsersByLikesInLast7Days();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", result[0]); // Korisničko ime
            userData.put("likeCount", result[1]); // Broj lajkova
            response.add(userData);
        }

        return ResponseEntity.ok(response);
    }
}

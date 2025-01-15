package com.project.onlybuns.controller;


import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // Endpoint to filter posts
    // Endpoint to count posts (return count instead of posts)
    @GetMapping("/posts")
    public ResponseEntity<Long> countPosts(
            @RequestParam int year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer week
    ) {
        long count = analyticsService.countPosts(year, month, week);
        return ResponseEntity.ok(count);
    }

    // Endpoint to count comments (return count instead of comments)
    @GetMapping("/comments")
    public ResponseEntity<Long> countComments(
            @RequestParam int year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer week
    ) {
        long count = analyticsService.countComments(year, month, week);
        return ResponseEntity.ok(count);
    }


    // Endpoint to count posts

}

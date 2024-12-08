package com.project.onlybuns.controller;


import com.project.onlybuns.DTO.UserStatistics;
import com.project.onlybuns.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserStatisticsController {

    @Autowired
    private UserStatisticsService userStatisticsService;

    @GetMapping("/user/stats")
    public UserStatistics getUserStats() {
        double postsPercentage = userStatisticsService.getUsersWithPostsPercentage();
        double commentsOnlyPercentage = userStatisticsService.getUsersWithCommentsOnlyPercentage();
        double noActivityPercentage = userStatisticsService.getUsersWithNoActivityPercentage();

        return new UserStatistics(postsPercentage, commentsOnlyPercentage, noActivityPercentage);
    }
}

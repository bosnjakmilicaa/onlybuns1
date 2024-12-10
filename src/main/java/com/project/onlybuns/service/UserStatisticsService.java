package com.project.onlybuns.service;

import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticsService {

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    public double getUsersWithPostsPercentage() {
        long totalUsers = registeredUserRepository.count();
        long usersWithPosts = registeredUserRepository.countByPostsIsNotNull();
        return (double) usersWithPosts / totalUsers * 100;
    }
    public double getUsersWithCommentsOnlyPercentage() {
        long totalUsers = registeredUserRepository.count();
        long usersWithCommentsOnly = registeredUserRepository.countByCommentsIsNotNullAndPostsIsEmpty();
        return (double) usersWithCommentsOnly / totalUsers * 100;
    }

    public double getUsersWithNoActivityPercentage() {
        long totalUsers = registeredUserRepository.count();
        long usersWithNoActivity = registeredUserRepository.countUsersWithNoActivity();
        return (double) usersWithNoActivity / totalUsers * 100;
    }

}

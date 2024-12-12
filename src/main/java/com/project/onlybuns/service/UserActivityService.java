package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.FollowLogRepository;
import com.project.onlybuns.repository.LikeRepository;
import com.project.onlybuns.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Service
public class UserActivityService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowLogRepository followRepository;

    @Autowired
    private LikeRepository likeRepository;

    public String generateWeeklySummary(RegisteredUser user) {
        // Generisanje statistike za poslednjih 7 dana
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        long newFollowers = followRepository.countByFollowedAndTimestampAfter(user, oneWeekAgo);
        long newLikes = likeRepository.countByPost_UserAndLikedAtAfter(user, oneWeekAgo);
        long newPosts = postRepository.countByUserAndCreatedAtAfter(user, oneWeekAgo);

        return String.format(
                "Dear %s,\n\nHere's your activity summary for the last 7 days:\n" +
                        "- New followers: %d\n" +
                        "- New likes on your posts: %d\n" +
                        "- New posts: %d\n\n" +
                        "We hope to see you back soon!\n\nBest regards,\nOnlyBuns Team",
                user.getFirstName(),
                newFollowers,
                newLikes,
                newPosts
        );
    }
}

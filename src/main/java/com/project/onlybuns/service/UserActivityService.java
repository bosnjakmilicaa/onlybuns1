package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.CommentRepository;
import com.project.onlybuns.repository.FollowRepository;
import com.project.onlybuns.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Service
public class UserActivityService {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public String generateWeeklySummary(RegisteredUser user) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        long newFollowers = followRepository.countByFollowedAndTimestampAfter(user, oneWeekAgo);
        long newLikes = likeRepository.countByPost_UserAndLikedAtAfter(user, oneWeekAgo);
        long newComments = commentRepository.countByPost_UserAndCreatedAtAfter(user, oneWeekAgo);

        return String.format(
                "Dear %s,\n\nHere's your activity summary for the last 7 days:\n" +
                        "- New followers: %d\n" +
                        "- New likes on your posts: %d\n" +
                        "- New comments on your posts: %d\n\n" +
                        "We hope to see you back soon!\n\nBest regards,\nOnlyBuns Team",
                user.getFirstName(),
                newFollowers,
                newLikes,
                newComments
        );
    }
}

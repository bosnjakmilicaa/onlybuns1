package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class AnalyticsService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public AnalyticsService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> filterPosts(int year, Integer month, Integer week) {
        return filterByDateRange(year, month, week, true);
    }

    public List<Comment> filterComments(int year, Integer month, Integer week) {
        return filterByDateRange(year, month, week, false);
    }

    private <T> List<T> filterByDateRange(int year, Integer month, Integer week, boolean isPost) {
        LocalDateTime startDate;
        LocalDateTime endDate;

        // Start from the beginning of the specified year
        startDate = LocalDateTime.of(year, 1, 1, 0, 0);

        if (month != null) {
            // Adjust to the specified month
            startDate = startDate.withMonth(month).with(TemporalAdjusters.firstDayOfMonth());
            endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        } else {
            // Default to the entire year if the month is not provided
            endDate = startDate.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        }

        if (week != null) {
            // Adjust to the specified week within the month/year
            LocalDate weekStart = startDate.toLocalDate().plusWeeks(week - 1).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            LocalDate weekEnd = weekStart.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

            // Ensure the week is within the calculated range for the month/year
            if (weekStart.isBefore(startDate.toLocalDate())) {
                weekStart = startDate.toLocalDate();
            }
            if (weekEnd.isAfter(endDate.toLocalDate())) {
                weekEnd = endDate.toLocalDate();
            }

            startDate = weekStart.atStartOfDay();
            endDate = weekEnd.atTime(LocalTime.MAX);
        }

        // Query the repository for posts or comments within the date range
        if (isPost) {
            return (List<T>) postRepository.findPostsByDateRange(startDate, endDate);
        } else {
            return (List<T>) commentRepository.findCommentsByDateRange(startDate, endDate);
        }
    }

    public long countPosts(int year, Integer month, Integer week) {
        return filterPosts(year, month, week).size();
    }

    public long countComments(int year, Integer month, Integer week) {
        return filterComments(year, month, week).size();
    }
}

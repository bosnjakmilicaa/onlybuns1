package com.project.onlybuns.service;

import com.project.onlybuns.model.Comment;
import com.project.onlybuns.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
    public long countCommentsForWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minus(1, ChronoUnit.WEEKS);
        return commentRepository.findCommentsByDateRange(startOfWeek, now).size();
    }

    public long countCommentsForMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.minus(1, ChronoUnit.MONTHS);
        return commentRepository.findCommentsByDateRange(startOfMonth, now).size();
    }

    public long countCommentsForYear() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYear = now.minus(1, ChronoUnit.YEARS);
        return commentRepository.findCommentsByDateRange(startOfYear, now).size();
    }

    public long countCommentsByUserInLastHour(Long userId) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minus(1, ChronoUnit.HOURS);
        return commentRepository.countByUserIdAndCreatedAtAfter(userId, oneHourAgo);
    }

    public List<Comment> findCommentsByPostSorted(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }
}

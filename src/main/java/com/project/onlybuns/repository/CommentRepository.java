package com.project.onlybuns.repository;

import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.createdAt >= :startDate AND c.createdAt <= :endDate")
    List<Comment> findCommentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByUserIdAndCreatedAtAfter(Long userId, LocalDateTime timestamp);

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    long countByPost_UserAndCreatedAtAfter(User user, LocalDateTime createdDate);
}

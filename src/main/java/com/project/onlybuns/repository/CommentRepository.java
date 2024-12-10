package com.project.onlybuns.repository;

import com.project.onlybuns.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Možeš dodati specifične metode za upite ako je potrebno

    @Query("SELECT c FROM Comment c WHERE c.createdAt >= :startDate AND c.createdAt <= :endDate")
    List<Comment> findCommentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

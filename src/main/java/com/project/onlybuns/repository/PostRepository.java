package com.project.onlybuns.repository;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll();  // Pronalazak svih objava
    List<Post> findByIsDeletedFalse();
    List<Post> findByUserUsername(String username);

    @Query("SELECT p FROM Post p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    List<Post> findPostsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT p.user.username FROM Post p WHERE p.user.id = :userId AND p.isDeleted = false")
    Optional<String> findUsernameByUserId(Long userId);

    List<Post> findByUser(RegisteredUser user); // Preporučeno

    @Query("SELECT p FROM Post p WHERE p.createdAt > :startDate")
    List<Post> findPostsAfterDate(@Param("startDate") LocalDateTime startDate);
    long countByUserAndCreatedAtAfter(User user, LocalDateTime createdDate);

    List<Post> findTop5ByCreatedAtAfterOrderByLikesDesc(LocalDateTime createdAt);
    List<Post> findTop10ByOrderByLikesDesc();
}





package com.project.onlybuns.repository;

import com.project.onlybuns.model.Like;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, RegisteredUser user);

    long countByPost_UserAndLikedAtAfter(User user, LocalDateTime createdDate);
}
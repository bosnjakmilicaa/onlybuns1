package com.project.onlybuns.repository;

import com.project.onlybuns.model.Follow;
import com.project.onlybuns.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    long countByFollowedAndTimestampAfter(User followed, LocalDateTime createdDate);
}

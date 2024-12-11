package com.project.onlybuns.repository;

import com.project.onlybuns.model.FollowLog;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FollowLogRepository extends JpaRepository<FollowLog, Long> {

    List<FollowLog> findByFollowerAndTimestampAfter(RegisteredUser follower, LocalDateTime timestamp);

    long countByFollowerAndTimestampAfter(RegisteredUser follower, LocalDateTime timestamp);

    long countByFollowedAndTimestampAfter(User followed, LocalDateTime createdDate);
}

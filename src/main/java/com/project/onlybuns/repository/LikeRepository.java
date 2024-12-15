package com.project.onlybuns.repository;

import com.project.onlybuns.model.Like;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, RegisteredUser user);

    long countByPost_UserAndLikedAtAfter(User user, LocalDateTime createdDate);

    @Query("SELECT l.user.username, COUNT(l) as likeCount " +
            "FROM Like l " +
            "WHERE l.likedAt >= :sevenDaysAgo " +
            "GROUP BY l.user.id, l.user.username " +
            "ORDER BY likeCount DESC")
    List<Object[]> findTop10UsersByLikesInLast7Days(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
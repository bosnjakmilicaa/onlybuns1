package com.project.onlybuns.service;

import com.project.onlybuns.model.Like;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public void likePost(Post post, RegisteredUser user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    public void unlikePost(Like like) {
        likeRepository.delete(like);
    }

    public Optional<Like> findByPostAndUser(Post post, RegisteredUser user) {
        return likeRepository.findByPostAndUser(post, user);
    }

    public List<Object[]> getTop10UsersByLikesInLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return likeRepository.findTop10UsersByLikesInLast7Days(sevenDaysAgo);
    }
}

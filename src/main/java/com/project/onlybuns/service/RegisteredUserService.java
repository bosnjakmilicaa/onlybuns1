package com.project.onlybuns.service;

import com.project.onlybuns.model.Follow;
import com.project.onlybuns.model.FollowLog;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.FollowLogRepository;
import com.project.onlybuns.repository.FollowRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;
    private final FollowLogRepository followLogRepository;

    private final FollowRepository followRepository;

    @Autowired
    public RegisteredUserService(RegisteredUserRepository registeredUserRepository, FollowRepository followRepository, FollowLogRepository followLogRepository) {
        this.registeredUserRepository = registeredUserRepository;
        this.followRepository = followRepository;
        this.followLogRepository = followLogRepository;
    }
    public Page<RegisteredUser> searchUsers(String firstName, String lastName, String email, Pageable pageable) {
        return registeredUserRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(
                firstName, lastName, email, pageable);
    }

    public Page<RegisteredUser> findAll(Pageable pageable) {
        return registeredUserRepository.findAll(pageable);
    }

    public List<RegisteredUser> findAll() {
        return registeredUserRepository.findAll();
    }

    public Optional<RegisteredUser> findById(Long id) {
        return registeredUserRepository.findById(id);
    }

    public RegisteredUser save(RegisteredUser user) {
        return registeredUserRepository.save(user);
    }

    public void delete(Long id) {
        registeredUserRepository.deleteById(id);
    }


    public boolean isAlreadyFollowing(Long followerId, Long followedId) {
        return followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    /*public void followUser(Long followerId, Long followedId) {
        RegisteredUser follower = registeredUserRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found."));
        RegisteredUser followed = registeredUserRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found."));

        // Proveri da li već postoji veza
        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new IllegalArgumentException("Already following this user.");
        }

        // Kreiraj novi Follow zapis
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        // Sačuvaj u bazi
        followRepository.save(follow);

        // Ažuriraj brojače
        follower.setFollowingCount(follower.getFollowingCount() + 1);
        followed.setFollowersCount(followed.getFollowersCount() + 1);

        // Sačuvaj promene u korisnicima
        registeredUserRepository.save(follower);
        registeredUserRepository.save(followed);
    }*/

    private boolean isFollowLimitExceeded(RegisteredUser follower) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        long count = followLogRepository.countByFollowerAndTimestampAfter(follower, oneMinuteAgo);
        return count >= 50;  // Ograničenje na 50 praćenja po minuti
    }

    /*public void followUser(Long followerId, Long followedId) {
        RegisteredUser follower = registeredUserRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found."));
        RegisteredUser followed = registeredUserRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found."));

        // Proveri da li korisnik premašuje ograničenje praćenja
        if (isFollowLimitExceeded(follower)) {
            throw new IllegalArgumentException("Follow limit exceeded. You can only follow 50 users per minute.");
        }

        // Proveri da li već postoji veza
        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new IllegalArgumentException("Already following this user.");
        }

        // Kreiraj novi Follow zapis
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        // Sačuvaj u bazi
        followRepository.save(follow);

        // Kreiraj zapis u FollowLog
        FollowLog followLog = new FollowLog();
        followLog.setFollower(follower);
        followLog.setFollowed(followed);
        followLog.setTimestamp(LocalDateTime.now());
        followLogRepository.save(followLog);

        // Ažuriraj brojače
        follower.setFollowingCount(follower.getFollowingCount() + 1);
        followed.setFollowersCount(followed.getFollowersCount() + 1);

        // Sačuvaj promene u korisnicima
        registeredUserRepository.save(follower);
        registeredUserRepository.save(followed);
    }*/

    @Transactional
    public void followUser(Long followerId, Long followedId) {

        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        RegisteredUser follower = registeredUserRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found."));
        RegisteredUser followed = registeredUserRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("Followed user not found."));

        if (isFollowLimitExceeded(follower)) {
            throw new IllegalArgumentException("Follow limit exceeded. You can only follow 50 users per minute.");
        }

        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new IllegalArgumentException("Already following this user.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        followRepository.save(follow);

        FollowLog followLog = new FollowLog();
        followLog.setFollower(follower);
        followLog.setFollowed(followed);
        followLog.setTimestamp(LocalDateTime.now());
        followLogRepository.save(followLog);

        // Simulacija sporog izvršavanja
        try {
            Thread.sleep(2000); // Pauza od 2 sekunde
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (this) {
            followed.setFollowersCount(followed.getFollowersCount() + 1);
            follower.setFollowingCount(follower.getFollowingCount() + 1);
        }

        registeredUserRepository.save(follower);
        registeredUserRepository.save(followed);
    }



    public void unfollowUser(Long followerId, Long followedId) {
        Follow follow = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship not found."));

        RegisteredUser follower = follow.getFollower();
        RegisteredUser followed = follow.getFollowed();

        // Obriši Follow zapis
        followRepository.delete(follow);

        // Ažuriraj brojače
        follower.setFollowingCount(follower.getFollowingCount() - 1);
        followed.setFollowersCount(followed.getFollowersCount() - 1);

        // Sačuvaj promene u korisnicima
        registeredUserRepository.save(follower);
        registeredUserRepository.save(followed);
    }

    public Long findUserIdByUsername(String username) {
        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found."));
        return user.getId();
    }

    public RegisteredUser getCurrentUserByUsername(String username) {
        Optional<RegisteredUser> optionalUser = registeredUserRepository.findByUsername(username);

        // Provera da li korisnik postoji, inače vraća izuzetak
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }
}

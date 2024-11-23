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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public boolean isAlreadyFollowing1(String followerUsername, String followedUsername) {
        // Pronalazak korisnika na osnovu korisničkog imena
        RegisteredUser follower = findByUsername(followerUsername);
        RegisteredUser followed = findByUsername(followedUsername);

        // Ako bilo koji korisnik nije pronađen, ne može biti praćenja
        if (follower == null || followed == null) {
            return false;
        }

        // Koristi postojeću metodu koja proverava praćenje na osnovu ID-jeva
        return isAlreadyFollowing(follower.getId(), followed.getId());
    }

    public RegisteredUser getCurrentUserByUsername(String username) {
        Optional<RegisteredUser> optionalUser = registeredUserRepository.findByUsername(username);

        // Provera da li korisnik postoji, inače vraća izuzetak
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }


    public RegisteredUser findByUsername(String username) {
        return registeredUserRepository.findByUsername(username)
                .orElse(null);
    }

    public Collection<RegisteredUser> getFollowers(String username) {
        RegisteredUser user = findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }

        return user.getFollowers()
                .stream()
                .map(Follow::getFollower) // Pretpostavka da Follow ima metodu getFollower()
                .collect(Collectors.toList());
    }

    public Collection<RegisteredUser> getFollowing(String username) {
        // Pronađi korisnika po username
        RegisteredUser user = findByUsername(username);

        // Proveri da li korisnik postoji
        if (user == null) {
            throw new IllegalArgumentException("User with username " + username + " not found.");
        }

        // Dohvati korisnike koje trenutni korisnik prati
        return user.getFollowing()
                .stream()
                .map(Follow::getFollowed) // Pretpostavka da Follow ima metodu getFollowed()
                .collect(Collectors.toList());
    }

}

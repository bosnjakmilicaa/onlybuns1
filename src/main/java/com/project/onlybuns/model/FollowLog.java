package com.project.onlybuns.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FollowLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RegisteredUser follower;

    @ManyToOne
    private RegisteredUser followed;

    private LocalDateTime timestamp;

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegisteredUser getFollower() {
        return follower;
    }

    public void setFollower(RegisteredUser follower) {
        this.follower = follower;
    }

    public RegisteredUser getFollowed() {
        return followed;
    }

    public void setFollowed(RegisteredUser followed) {
        this.followed = followed;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

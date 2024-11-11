package com.project.onlybuns.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private RegisteredUser follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private RegisteredUser followed;

    // Getteri i setteri
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
}


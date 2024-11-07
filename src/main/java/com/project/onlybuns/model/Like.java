package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference(value = "post-likes")  // Sprečava serijalizaciju povezane post objekta
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-likes") // Sprečava serijalizaciju povezane RegisteredUser objekta
    private RegisteredUser user;

    private LocalDateTime likedAt;

    // Constructors, getters, and setters
    public Like() {
    }

    public Like(Post post, RegisteredUser user) {
        this.post = post;
        this.user = user;
        this.likedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public RegisteredUser getUser() {
        return user;
    }

    public void setUser(RegisteredUser user) {
        this.user = user;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }

    public void setUserId(Long userId) {
        this.user = new RegisteredUser();
        this.user.setId(userId); // Set the user ID using the provided userId
    }
}

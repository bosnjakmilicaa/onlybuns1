package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;  // URL slike

    @Column(nullable = true)
    private String description; // Opis slike

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-posts")
    private RegisteredUser user; // Connection to RegisteredUser

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "post-comments")
    private List<Comment> comments = new ArrayList<>(); // Comments associated with the post

    @ManyToMany // Koristite ManyToMany ili OneToMany zavisno od dizajna
    @JoinTable(
            name = "post_likes", // Ime tabele koja će povezivati postove i korisnike
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<RegisteredUser> likedByUsers = new HashSet<>(); // Users who liked the post

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // New field for creation date

    // New column to store like count
    @Column(nullable = false)
    private int countLikes;  // Initialize with 0 likes

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();  // Set the creation timestamp before persisting the post
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter and setter for countLikes
    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    // Other constructors and methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description; // Getter za opis
    }

    public void setDescription(String description) {
        this.description = description; // Setter za opis
    }

    public RegisteredUser getUser() {
        return user;
    }

    public void setUser(RegisteredUser user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<RegisteredUser> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(Set<RegisteredUser> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void addLikedUser(RegisteredUser user) {
        this.likedByUsers.add(user);
        this.countLikes = this.likedByUsers.size(); // Update countLikes when a user likes the post
    }

    public void removeLikedUser(RegisteredUser user) {
        this.likedByUsers.remove(user);
        this.countLikes = this.likedByUsers.size(); // Update countLikes when a user removes like
    }

    // Return user ID from the RegisteredUser object
    public Long getUserId() {
        return user != null ? user.getId() : null; // Return user ID or null if user is not set
    }

}

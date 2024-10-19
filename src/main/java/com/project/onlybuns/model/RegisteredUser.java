package com.project.onlybuns.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RegisteredUser extends User {

    private String profileInfo;  // Profile information

    @ManyToMany
    @JoinTable(
            name = "user_likes_post", // Name of the table that connects users and posts
            joinColumns = @JoinColumn(name = "user_id"), // Column for users
            inverseJoinColumns = @JoinColumn(name = "post_id") // Column for posts
    )
    private List<Post> likedPosts = new ArrayList<>(); // List of posts liked by the user

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>(); // List of posts created by the user

    // Default constructor
    public RegisteredUser() {
        super("", ""); // Call superclass constructor with default values
    }

    // Constructor with parameters
    public RegisteredUser(String username, String password, String profileInfo) {
        super(username, password); // Call superclass constructor with parameters
        this.profileInfo = profileInfo;
    }

    // Getter and Setter for profileInfo
    public String getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(String profileInfo) {
        this.profileInfo = profileInfo;
    }

    // Getter for likedPosts
    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    // Getter for posts
    public List<Post> getPosts() {
        return posts;
    }

    // Method to manage posts
    public void addPost(Post post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
        post.setUser(this);  // Set the user who created the post
    }

    public void removePost(Post post) {
        if (posts != null) {
            posts.remove(post);
            post.setUser(null);  // Remove the association with the user
        }
    }

    // Methods to like and unlike posts
    public void likePost(Post post) {
        if (!likedPosts.contains(post)) {
            likedPosts.add(post);
            post.getLikedByUsers().add(this); // Add the user to the list of users who liked the post
        }
    }

    public void unlikePost(Post post) {
        if (likedPosts.contains(post)) {
            likedPosts.remove(post);
            post.getLikedByUsers().remove(this); // Remove the user from the list of users who liked the post
        }
    }
}

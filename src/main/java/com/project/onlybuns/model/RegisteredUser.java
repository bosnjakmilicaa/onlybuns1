package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorValue("registered_user")
public class RegisteredUser extends User {

    @ManyToMany
    @JoinTable(
            name = "user_likes_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> likedPosts = new ArrayList<>(); // List of posts liked by the user

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>(); // List of posts created by the user

    @ManyToOne // Dodaj ovu vezu
    @JoinColumn(name = "admin_user_id") // Ovo će biti naziv kolone u tabeli RegisteredUser
    private AdminUser adminUser; // Referenca ka AdminUser

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>(); // List of likes given by the user

    // Getters and Setters for posts
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getNumberOfPosts() {
        return posts.size(); // Vraća broj postova
    }

    // Getters and Setters for likes
    public List<Like> getLikes() {
        return likes; // Vraća listu lajkova korisnika
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes; // Postavlja listu lajkova korisnika
    }

    // Metod za brojanje lajkova
    public int getNumberOfLikes() {
        return likes.size(); // Vraća broj lajkova
    }

    // Metod za dodavanje lajka
    public void addLike(Like like) {
        if (!likes.contains(like)) {
            likes.add(like);
        }
    }

    // Metod za uklanjanje lajka
    public void removeLike(Like like) {
        likes.remove(like);
    }

    // Metod za dodavanje lajkova na postove
    public void addLikedPost(Post post) {
        if (!likedPosts.contains(post)) {
            likedPosts.add(post);
        }
    }

    // Metod za uklanjanje lajkova sa postova
    public void removeLikedPost(Post post) {
        likedPosts.remove(post);
    }

    // Getters and Setters for adminUser
    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}

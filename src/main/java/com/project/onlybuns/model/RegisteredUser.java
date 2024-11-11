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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "post-user") // Povezano sa `@JsonBackReference` u `Post` klasi
    private List<Post> posts = new ArrayList<>();

    @ManyToOne // Dodaj ovu vezu
    @JoinColumn(name = "admin_user_id") // Ovo će biti naziv kolone u tabeli RegisteredUser
    private AdminUser adminUser; // Referenca ka AdminUser

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>(); // List of likes given by the user




    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>(); // Korisnici koje ovaj korisnik prati

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>(); // Korisnici koji prate ovog korisnika


    // Getteri i setteri za `following` i `followers`
    public List<Follow> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follow> following) {
        this.following = following;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }


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


    public int getFollowingCount() {
        return following.size();
    }
}

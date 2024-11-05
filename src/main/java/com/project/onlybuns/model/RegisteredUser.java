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

    public List<Post> getPosts() {
        return posts;
    }
    public int getNumberOfPosts() {
        return posts.size(); // Vraća broj postova
    }

    // ... ostali metodi i getteri/setteri




}

package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("registered_user")
public class RegisteredUser extends User {

    @Email // Ensures that the email is valid
    @NotBlank(message = "Email is required")
    private String email; // Email address for login and activation

    @NotBlank(message = "First name is required")
    private String firstName; // First name of the user

    @NotBlank(message = "Last name is required")
    private String lastName; // Last name of the user

    @NotBlank(message = "Address is required")
    private String address; // User's address

    private boolean isActive; // Status of the user's account

    private String profileInfo;  // Profile information

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

    // ... ostali metodi i getteri/setteri

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }



}

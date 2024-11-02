package com.project.onlybuns.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("admin")
public class AdminUser extends User {

    /*@Email // Validates that the email is in a proper format
    @NotBlank(message = "Email is required")
    private String email; // Email address for login

    private boolean isActive; // Status of the admin's account

    @OneToMany(mappedBy = "adminUser") // mappedBy should point to the correct field in RegisteredUser
    private List<RegisteredUser> registeredUsers = new ArrayList<>(); // List of registered users managed by this admin

    public AdminUser() {
        super("", ""); // Call superclass constructor with default values
        this.isActive = false; // By default, admin user is not active
    }

    public AdminUser(String username, String password, String email) {
        super(username, password); // Call superclass constructor with parameters
        this.email = email;
        this.isActive = false; // New admin starts inactive
    }*/

    private String email;

    private boolean isActive;

    public AdminUser() {
        super("", "");
        this.isActive = false;
    }

    public AdminUser(String username, String password, String email) {
        super(username, password);
        this.email = email;
        this.isActive = false;
    }

    // ... ostali metodi i getteri/setteri
}

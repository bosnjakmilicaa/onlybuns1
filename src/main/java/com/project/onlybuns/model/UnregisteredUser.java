package com.project.onlybuns.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("unregistered_user")
public class UnregisteredUser extends User {

    private String email;
    private String firstName;
    private String lastName;
    private String address; // User's address
    private boolean isActive; // Status of the user's account

    // Default constructor
    public UnregisteredUser() {
        super();
        this.isActive = false; // By default, user is not active
    }

    // Constructor with parameters
    public UnregisteredUser(String username, String password, String email, String firstName, String lastName, String address) {
        super(username, password);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.isActive = false; // New user starts inactive
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Method to validate user input during registration
    public boolean validateRegistration() {
        // Add validation logic for email, username, and password
        // For example, check if fields are not empty, if email is valid, etc.
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return false; // Invalid email
        }
        if (getUsername() == null || getUsername().isEmpty()) {
            return false; // Invalid username
        }
        if (getPassword() == null || getPassword().length() < 6) {
            return false; // Password must be at least 6 characters
        }
        // More validation can be added as needed
        return true;
    }
}

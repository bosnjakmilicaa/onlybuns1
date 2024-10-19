package com.project.onlybuns.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Only the root class should have this annotation
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Define inheritance strategy
@DiscriminatorColumn(name = "user_type") // Discriminator column for subclasses
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // No-argument constructor
    public User() {
    }

    // Constructor with parameters
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

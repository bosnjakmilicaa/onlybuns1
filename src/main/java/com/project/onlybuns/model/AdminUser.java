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

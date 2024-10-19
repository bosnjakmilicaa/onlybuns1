package com.project.onlybuns.model;

import jakarta.persistence.Entity;

@Entity
public class UnregisteredUser extends User {

    public UnregisteredUser() {
        super();
    }

    public UnregisteredUser(String username, String password) {
        super(username, password);
    }

}

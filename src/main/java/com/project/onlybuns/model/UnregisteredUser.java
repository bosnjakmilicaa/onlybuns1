package com.project.onlybuns.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("unregistered_user")
public class UnregisteredUser extends User {

    public UnregisteredUser() {
        super();
    }

    public UnregisteredUser(String username, String password) {
        super(username, password);
    }

}

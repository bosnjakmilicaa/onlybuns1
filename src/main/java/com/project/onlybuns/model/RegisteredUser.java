package com.project.onlybuns.model;

import jakarta.persistence.Entity;
import java.util.List;

@Entity
public class RegisteredUser extends User {

    private String profileInfo;  // Informacije o profilu

    public RegisteredUser() {
        super();
    }

    public RegisteredUser(String username, String password, String profileInfo) {
        super(username, password);
        this.profileInfo = profileInfo;
    }

    // Getteri i setteri za profileInfo
    public String getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(String profileInfo) {
        this.profileInfo = profileInfo;
    }

    // Metode za lajkovanje, komentarisanje, postavljanje objava itd.
}

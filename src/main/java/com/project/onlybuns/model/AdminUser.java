package com.project.onlybuns.model;

import jakarta.persistence.Entity;

@Entity
public class AdminUser extends User {

    public AdminUser() {
        super();
    }

    public AdminUser(String username, String password) {
        super(username, password);
    }

    // Metode za upravljanje korisnicima, objavama i izveštajima
    public void deletePost(Long postId) {
        // Logika za brisanje objave
    }

    public void registerAdmin(AdminUser newAdmin) {
        // Logika za registrovanje novih administratora
    }
}

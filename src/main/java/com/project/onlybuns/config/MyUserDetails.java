package com.project.onlybuns.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String username;
    private String password; // Čuvaj lozinku ako je potrebno
    private List<GrantedAuthority> authorities;

    public MyUserDetails(String username,  List<String> roles) {
        this.username = username;
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new) // Pretvara ulogu u GrantedAuthority
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Vraća listu autoriteta
    }

    @Override
    public String getPassword() {
        return password; // Vraća lozinku
    }

    @Override
    public String getUsername() {
        return username; // Vraća korisničko ime
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Ovo se može promeniti na osnovu vaših poslovnih pravila
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Ovo se može promeniti na osnovu vaših poslovnih pravila
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ovo se može promeniti na osnovu vaših poslovnih pravila
    }

    @Override
    public boolean isEnabled() {
        return true; // Ovo se može promeniti na osnovu vaših poslovnih pravila
    }
}

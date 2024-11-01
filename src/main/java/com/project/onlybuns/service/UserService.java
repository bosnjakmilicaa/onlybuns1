package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.AdminUserRepository;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import com.project.onlybuns.repository.UnregisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda za vraćanje svih korisnika


    public List<User> findAllUsers() {
        return userRepository.findAll(); // Ova metoda bi trebala vraćati sve korisnike kao User
    }
    /*public List<RegisteredUser> findAll() {
        return userRepository.findAll();
    }*/

    /*public Optional<RegisteredUser> findById(Long id) {
        return userRepository.findById(id);
    }*/

    public RegisteredUser save(RegisteredUser user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    /*public RegisteredUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }*/
}

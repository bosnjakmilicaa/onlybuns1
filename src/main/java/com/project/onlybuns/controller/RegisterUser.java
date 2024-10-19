package com.project.onlybuns.controller;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegisterUser {

    @Autowired
    private UserRepository userRepository;

    // Endpoint za registraciju korisnika
    @PostMapping("/register")
    public String registerUser(@RequestBody RegisteredUser newUser) {
        userRepository.save(newUser);
        return "Successfull registered";
    }
}

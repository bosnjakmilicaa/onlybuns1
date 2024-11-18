package com.project.onlybuns.controller;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // Osnovna putanja za korisničke operacije
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("userType") != null && session.getAttribute("userType").equals("ADMIN");
    }


    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) {
        RegisteredUser createdUser = userService.save(user);
        return ResponseEntity.ok(createdUser);
    }
    @PostMapping("/update-passwords")
    public ResponseEntity<String> updatePasswords() {
        userService.updatePasswords();
        return ResponseEntity.ok("Passwords updated successfully.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/username/{username}")
    public ResponseEntity<RegisteredUser> getUserByUsername(@PathVariable String username) {
        RegisteredUser user = userService.findByUsername(username);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }*/
}

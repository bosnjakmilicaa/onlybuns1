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

    /*@GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            HttpSession session) {
        if (isAdmin(session)) {
            List<User> users = userService.searchUsers(firstName, lastName, email, minPosts, maxPosts);
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }*/

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) Boolean sortByFollowers,
            HttpSession session) {
        if (isAdmin(session)) {
            List<User> users = userService.searchUsers(firstName, lastName, email, minPosts, maxPosts, sortByFollowers);
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }




    /*@GetMapping
    public ResponseEntity<List<RegisteredUser>> getAllUsers() {
        List<RegisteredUser> users = userService.findAll();
        return ResponseEntity.ok(users);
    }*/


    /*@GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }*/

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

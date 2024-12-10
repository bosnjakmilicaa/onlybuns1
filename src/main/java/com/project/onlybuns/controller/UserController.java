package com.project.onlybuns.controller;

import com.project.onlybuns.DTO.RegisteredUserDTO;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUserDetails(
            @PathVariable String username,
            @RequestBody Map<String, String> requestBody) {
        if (requestBody.containsKey("firstName")) {
            userService.updateFirstName(username, requestBody.get("firstName"));
        }
        if (requestBody.containsKey("email")) {
            userService.changeEmail(username, requestBody.get("email"));
        }
        if (requestBody.containsKey("lastName")) {
            userService.updateLastName(username, requestBody.get("lastName"));
        }
        if (requestBody.containsKey("adress")) {
            userService.updateLastName(username, requestBody.get("adress"));
        }
        if (requestBody.containsKey("oldPassword") && requestBody.containsKey("newPassword")) {
            boolean isUpdated = userService.updatePassword(
                    username,
                    requestBody.get("oldPassword"),
                    requestBody.get("newPassword")
            );
            if (!isUpdated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Neispravna stara šifra
            }
        }
        return ResponseEntity.ok().build();
    }




    /*@GetMapping("/username/{username}")
    public ResponseEntity<RegisteredUser> getUserByUsername(@PathVariable String username) {
        RegisteredUser user = userService.findByUsername(username);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }*/
}

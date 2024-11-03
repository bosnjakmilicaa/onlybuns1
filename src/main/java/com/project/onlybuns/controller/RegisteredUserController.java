package com.project.onlybuns.controller;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.RegisteredUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/registered-users") // Base path for registered user-related endpoints
@PreAuthorize("hasRole('ADMIN')")
public class RegisteredUserController {

    private final RegisteredUserService registeredUserService;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService) {
        this.registeredUserService = registeredUserService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRegisteredUsers(HttpSession session) {
        // Proveri da li je ulogovani korisnik administrator
        Object userType = session.getAttribute("userType");
        if (userType != null && userType.equals("ADMIN")) {
            List<RegisteredUser> registeredUsers = registeredUserService.findAll();
            return ResponseEntity.ok(registeredUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "Error: You do not have permission to access this resource."));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RegisteredUser>> getAllUsers() {
        List<RegisteredUser> users = registeredUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUserById(@PathVariable Long id) {
        return registeredUserService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) {
        RegisteredUser createdUser = registeredUserService.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        registeredUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

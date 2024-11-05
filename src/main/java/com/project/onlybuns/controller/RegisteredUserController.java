package com.project.onlybuns.controller;
import com.project.onlybuns.service.UserService;
import org.springframework.security.core.Authentication;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.RegisteredUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController// Base path for registered user-related endpoints
public class RegisteredUserController {

    private final RegisteredUserService registeredUserService;
    private final UserService registeredUserService1;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService,UserService registeredUserService1) {
        this.registeredUserService = registeredUserService;
        this.registeredUserService1 = registeredUserService1;

    }

    @GetMapping("/registered-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredUser>> getRegisteredUsers() {
        // Logovanje za debugging
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dobijanje registrovanih korisnika
        List<RegisteredUser> registeredUsers = registeredUserService.findAll();
        return ResponseEntity.ok(registeredUsers);
    }

    @GetMapping("/searchReg")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredUser>> searchRegisteredUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) Boolean sortByFollowers) {

        List<RegisteredUser> users = registeredUserService1.searchRegisteredUsers(firstName, lastName, email, minPosts, maxPosts, sortByFollowers);
        return ResponseEntity.ok(users);
    }




    /*@GetMapping("/registered-users")
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
    }*/




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

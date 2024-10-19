package com.project.onlybuns.controller;

import com.project.onlybuns.model.UnregisteredUser;
import com.project.onlybuns.service.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unregistered-users") // Base path for unregistered user-related endpoints
public class UnregisteredUserController {

    private final UnregisteredUserService unregisteredUserService;

    @Autowired
    public UnregisteredUserController(UnregisteredUserService unregisteredUserService) {
        this.unregisteredUserService = unregisteredUserService;
    }

    @GetMapping
    public ResponseEntity<List<UnregisteredUser>> getAllUsers() {
        List<UnregisteredUser> users = unregisteredUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnregisteredUser> getUserById(@PathVariable Long id) {
        return unregisteredUserService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UnregisteredUser> createUser(@RequestBody UnregisteredUser user) {
        UnregisteredUser createdUser = unregisteredUserService.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        unregisteredUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

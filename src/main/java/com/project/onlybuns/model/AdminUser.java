package com.project.onlybuns.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Entity
@DiscriminatorValue("admin") // Set value for user type differentiation
public class AdminUser extends User {

    public AdminUser() {
        super();
    }

    @GetMapping("/my-endpoint")
    public ResponseEntity<String> myEndpoint() {
        return ResponseEntity.ok("This is my endpoint!");
    }

    public AdminUser(String username, String password) {
        super(username, password);
    }

    // Methods for managing users, posts, and reports
    public void deletePost(Long postId, List<Post> posts) {
        posts.removeIf(post -> post.getId().equals(postId));
    }

    public void registerAdmin(AdminUser newAdmin, List<AdminUser> admins) {
        admins.add(newAdmin);
    }

    public List<Post> viewReports(List<Post> posts) {
        return posts; // Returns all posts as a report
    }
}

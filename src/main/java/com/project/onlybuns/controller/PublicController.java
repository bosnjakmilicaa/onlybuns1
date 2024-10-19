package com.project.onlybuns.controller;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.UserProfile;
import com.project.onlybuns.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PublicService publicService;

    // Endpoint za pregled svih objava
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return publicService.getAllPosts();
    }

    // Endpoint za pregled profila korisnika
    @GetMapping("/profile/{username}")
    public UserProfile getUserProfile(@PathVariable String username) {
        return publicService.getUserProfile(username);
    }
}

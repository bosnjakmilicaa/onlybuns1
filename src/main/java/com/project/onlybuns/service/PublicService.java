package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.UserProfile;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    // Pregled svih objava
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Pregled profila korisnika
    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username);
    }
}

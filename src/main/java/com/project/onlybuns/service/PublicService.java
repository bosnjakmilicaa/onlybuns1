package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.UserProfile;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublicService {

    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public PublicService(PostRepository postRepository, UserProfileRepository userProfileRepository) {
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
    }

    // Pregled svih objava
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Pregled profila korisnika
    public Optional<UserProfile> getUserProfile(String username) {
        return Optional.ofNullable(userProfileRepository.findByUsername(username));
    }
}

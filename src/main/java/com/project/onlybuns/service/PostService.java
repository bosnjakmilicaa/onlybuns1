package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.PostNotFoundException;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    /*public void delete(Long id) {
        postRepository.deleteById(id);
    }*/

    public List<Post> findAllActivePosts() {
        return postRepository.findByIsDeletedFalse(); // Metoda koja vraća sve aktivne objave
    }

    @Transactional
    public Post update(Post updatedPost) {
        // Proveri da li post sa datim ID-om postoji
        Post existingPost = postRepository.findById(updatedPost.getId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + updatedPost.getId()));

        // Proveri da li su svi potrebni podaci ispravni pre ažuriranja
        if (updatedPost.getImageUrl() == null || updatedPost.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Image URL must not be null or empty.");
        }
        if (updatedPost.getDescription() == null) {
            throw new IllegalArgumentException("Description must not be null.");
        }

        // Ažuriraj atribute postojeće objave
        existingPost.setImageUrl(updatedPost.getImageUrl());
        existingPost.setDescription(updatedPost.getDescription());

        // Sačuvaj ažuriranu objavu u bazi
        return postRepository.save(existingPost);
    }
    public void delete(Long id) {
        postRepository.deleteById(id); // Ova metoda ne treba da vraća ništa
    }
    @Autowired
    private RegisteredUserRepository registeredUserRepository; // Dodajte ovo

    public List<Post> findByUserId(Long userId) {
        RegisteredUser user = registeredUserRepository.findById(userId)
                .orElse(null); // Pronađite korisnika po ID-ju

        if (user != null) {
            return postRepository.findByUser(user); // Koristite korisnika da dobijete postove
        }
        return Collections.emptyList(); // Vratite praznu listu ako korisnik nije pronađen
    }


    public void likePost(Long id, RegisteredUser user) {
        // Prvo proveri da li post postoji
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // Proveri da li je korisnik već lajkovao ovu objavu
        if (post.getLikedByUsers().contains(user)) {
            throw new RuntimeException("User has already liked this post.");
        }


        // Dodaj korisnika u listu lajkova objave
        post.getLikedByUsers().add(user);
        postRepository.save(post); // Sačuvaj promene u bazi
    }

}

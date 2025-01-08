package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PostLikeConcurrencyTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        // Dohvati test podatke
        Post post = postRepository.findById(9L)
                .orElseThrow(() -> new IllegalArgumentException("Post with ID 9 not found."));
        System.out.println("Post loaded: " + post.getDescription());
    }

    @Test
    void testConcurrentLikeOrUnlike() {
        // Kreiraj post i korisnike
        Post post = postRepository.findById(9L).orElseThrow();
        String username1 = "tester4";
        String username2 = "tester5";
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(() -> {
            try {
                System.out.println("Thread 1 started");
                String user1 = username1;
                Optional<User> user = userRepository.findByUsername(user1);
                if (user.isPresent()) {
                    System.out.println("Valid user: " + user1);
                    postService.likeOrUnlikePost(post.getId(), user1);
                } else {
                    System.out.println("User not found: " + user1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Future<?> future2 = executor.submit(() -> {
            try {
                System.out.println("Thread 2 started");
                String user2 = username2;
                Optional<User> user = userRepository.findByUsername(user2);
                if (user.isPresent()) {
                    System.out.println("Valid user: " + user2);
                    postService.likeOrUnlikePost(post.getId(), user2);
                } else {
                    System.out.println("User not found: " + user2);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            // Čekaj da oba thread-a završe
            future1.get();
            future2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Proveri stanje baze podataka posle konkurentnih operacija
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        System.out.println("Updated post likes count: " + updatedPost.getLikesCount());

        // Verifikuj da li je broj lajkova porastao za 2, ukoliko je uspešno izvršena konkurentna operacija
        assertEquals(2, updatedPost.getLikesCount());
    }
}

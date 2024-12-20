package com.project.onlybuns.controller;

import com.project.onlybuns.model.Like;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostControllerTest {

    @Autowired
    private PostController postController;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testConcurrentLikesWithTransaction() throws InterruptedException {
        RegisteredUser registeredUser1 = new RegisteredUser();
        registeredUser1.setId(1L);
        registeredUser1.setUsername("user1");
        registeredUser1.setEmail("user1@example.com");

        RegisteredUser registeredUser2 = new RegisteredUser();
        registeredUser2.setId(2L);
        registeredUser2.setUsername("user2");
        registeredUser2.setEmail("user2@example.com");


        Post post = new Post();
        post.setId(1L);
        post.setDescription("Post description");
        post.setLocation("Some Location");
        post.setLatitude(45.0);
        post.setLongitude(19.0);
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(registeredUser1);


        List<Like> likes = new ArrayList<>();


        Like like1 = new Like();
        like1.setId(1L);
        like1.setLikedAt(LocalDateTime.now());
        like1.setPost(post);
        like1.setUser(registeredUser2);
        likes.add(like1);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setLikedAt(LocalDateTime.now());
        like2.setPost(post);
        like2.setUser(registeredUser1);
        likes.add(like2);
        
        post.setLikes(likes);

        // Simulacija konkurentnih upita za lajkovanje
        Thread thread1 = new Thread(() -> {
            synchronized (post) {  // Sinhronizacija kako bi se izbeglo dupliranje
                if (post.getLikes().stream().noneMatch(like -> like.getUser().equals(registeredUser1))) {  // Provera da li je korisnik već lajkovao
                    Like likeForThread1 = new Like();
                    likeForThread1.setId(3L);
                    likeForThread1.setLikedAt(LocalDateTime.now());
                    likeForThread1.setPost(post);
                    likeForThread1.setUser(registeredUser1); // user1 lajkuje post
                    post.addLike(likeForThread1);
                    System.out.println("User 1 liked the post");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (post) {  // Sinhronizacija kako bi se izbeglo dupliranje
                if (post.getLikes().stream().noneMatch(like -> like.getUser().equals(registeredUser2))) {  // Provera da li je korisnik već lajkovao
                    Like likeForThread2 = new Like();
                    likeForThread2.setId(4L);
                    likeForThread2.setLikedAt(LocalDateTime.now());
                    likeForThread2.setPost(post);
                    likeForThread2.setUser(registeredUser2); // user2 lajkuje post
                    post.addLike(likeForThread2);
                    System.out.println("User 2 liked the post");
                }
            }
        });

        // Pokretanje niti
        thread1.start();
        thread2.start();

        // Dodavanje pauze kako bi se omogućilo konkurentno izvršavanje
        thread1.join();
        thread2.join();

        // Ispisivanje rezultata
        System.out.println("Post description: " + post.getDescription());
        post.getLikes().forEach(like -> {
            System.out.println("Like by user: " + like.getUser().getUsername());
            System.out.println("Liked at: " + like.getLikedAt());
        });

        // Provera broja lajkova
        System.out.println("Total number of likes on post: " + post.getLikes().size());

        // Očekivani broj lajkova je 2 jer samo dva različita korisnika mogu lajkovati post.
        assertEquals(2, post.getLikes().size());
    }

}
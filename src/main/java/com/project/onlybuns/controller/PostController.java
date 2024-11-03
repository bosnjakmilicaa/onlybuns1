package com.project.onlybuns.controller;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }


    /*@GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getPostsByUserId(@AuthenticationPrincipal RegisteredUser user) {
        List<Post> userPosts = postService.findByUserId(user.getId());
        return ResponseEntity.ok(userPosts);
    }*/

    @GetMapping("/my-posts")
    public ResponseEntity<?> getPostsByUserId(HttpSession session) {
        User user = (User) session.getAttribute("user"); // Dobavi korisnika iz sesije

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Error: User not logged in!"));
        }

        // Proveri tip korisnika
        String userType = (String) session.getAttribute("userType");
        if (!"REGISTERED".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "Error: Access denied for non-registered users!"));
        }

        Long userId = user.getId(); // Uzmimo userId iz korisnika

        List<Post> userPosts = postService.findByUserId(userId);
        return ResponseEntity.ok(userPosts);
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.findAllActivePosts(); // Treba implementirati ovu metodu
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, @AuthenticationPrincipal RegisteredUser user) {
        post.setUser(user); // Postavi korisnika koji pravi objavu
        Post createdPost = postService.save(post);
        return ResponseEntity.ok(createdPost);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost, HttpSession session) {
        // Proveri da li je korisnik prijavljen i da li je registrovan
        RegisteredUser loggedUser = (RegisteredUser) session.getAttribute("user");
        String userType = (String) session.getAttribute("userType");

        // Proveri da li je korisnik prijavljen
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Vraća 401 ako korisnik nije prijavljen
        }

        // Proveri da li je korisnik administrator
        if ("ADMIN".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Vraća 403 ako je korisnik administrator
        }

        // Proveri da li post postoji i da li je korisnik autor objave
        return postService.findById(id)
                .filter(post -> post.getUser().equals(loggedUser)) // Proveri da li korisnik može da menja objavu
                .map(post -> {
                    updatedPost.setId(id); // Postavi ID za ažuriranje
                    Post savedPost = postService.update(updatedPost);
                    return ResponseEntity.ok(savedPost);
                })
                .orElse(ResponseEntity.notFound().build()); // Vraća 404 ako post ne postoji ili korisnik nije autor
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, HttpSession session) {
        // Proveri da li je korisnik prijavljen
        RegisteredUser loggedUser = (RegisteredUser) session.getAttribute("user");
        String userType = (String) session.getAttribute("userType");

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Vraća 401 ako korisnik nije prijavljen
        }

        // Proveri da li je korisnik administrator
        if ("ADMIN".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Vraća 403 ako je korisnik administrator
        }

        // Proveri da li post postoji i da li je korisnik autor objave
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.getUser().equals(loggedUser)) { // Proveri da li korisnik može da obriše objavu
                postService.delete(id); // Ova metoda ne treba da vraća ništa
                return ResponseEntity.noContent().build(); // Vraća 204 ako je obrisano
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Vraća 403 ako korisnik nije autor
            }
        } else {
            return ResponseEntity.notFound().build(); // Vraća 404 ako post ne postoji
        }
    }




    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id, @AuthenticationPrincipal RegisteredUser user) {
        postService.likePost(id, user); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long id, @RequestBody Comment comment, @AuthenticationPrincipal RegisteredUser user) {
        comment.setUser(user);
        comment.setPost(postService.findById(id).orElse(null)); // Setuj post za komentar
        Comment savedComment = commentService.save(comment); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok(savedComment);
    }
}

package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;
import com.project.onlybuns.model.*;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    private final UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }


    /*@GetMapping("/my-posts")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<Post> getPostsForLoggedInUser() {
        // Dobijanje trenutnog korisnika iz SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.getPostsByUsername(username);
    }*/

    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<Map<String, Object>> getPostsForLoggedInUser() {
        // Dobijanje trenutnog korisnika iz SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Post> posts = postService.getPostsByUsername(username);
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();

            // Dodajemo podatke o postu
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime
            postData.put("countLikes", post.getLikesCount());
            // Dodajemo listu komentara
            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime komentara
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            // Dodajemo post sa komentarima
            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
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
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        // Dobijanje trenutno prijavljenog korisnika
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

        // Proveri da li je korisnik prijavljen
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 401 Unauthorized
        }

        // Pronađi post po ID-ju i proveri da li je autor isti kao prijavljeni korisnik
        return postService.findById(id)
                .filter(post -> post.getUser().equals(loggedUser)) // Proveri da li je prijavljeni korisnik autor posta
                .map(post -> {
                    // Ažuriraj podatke posta
                    post.setImageUrl(updatedPost.getImageUrl());
                    post.setDescription(updatedPost.getDescription());
                    post.setDeleted(updatedPost.isDeleted());

                    // Ažuriraj listu komentara samo ako je to neophodno (opcionalno)
                    if (updatedPost.getComments() != null) {
                        post.setComments(updatedPost.getComments());
                    }

                    // Postavi korisnika ponovo kako bi se osiguralo da je pravilno povezan
                    post.setUser(loggedUser);

                    // Sačuvaj izmenjeni post
                    Post savedPost = postService.update(post);
                    return ResponseEntity.ok(savedPost);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404 Not Found
    }

    /*@GetMapping("/allPosts")
    public List<Post> getAllPosts() {
        return postService.findAllActivePosts();
    }*/

    /*@GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts() {
        List<Post> posts = postService.findAllActivePosts();
        List<Map<String, Object>> postsWithUsernames = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime
            postsWithUsernames.add(postData);
        }

        return postsWithUsernames;
    }*/

    /*@GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts() {
        List<Post> posts = postService.findAllActivePosts();
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();

            // Dodajemo podatke o postu
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime

            // Dodajemo listu komentara
            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime komentara
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            // Dodajemo post sa komentarima
            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }*/

    @GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts() {
        List<Post> posts = postService.findAllActivePosts();
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // Format za datum i vreme

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();

            // Dodajemo podatke o postu
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown");
            postData.put("countLikes", post.getLikesCount());
            // Formatiramo datum i vreme za post
            String formattedDate = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "Unknown date";
            postData.put("createdAt", formattedDate); // Dodajemo datum i vreme

            // Dodajemo listu komentara
            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown");

                // Formatiramo datum i vreme za komentar
                String formattedCommentDate = comment.getCreatedAt() != null ? comment.getCreatedAt().format(formatter) : "Unknown date";
                commentData.put("createdAt", formattedCommentDate); // Dodajemo datum i vreme za komentar

                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            // Dodajemo post sa komentarima
            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }

    /*@PutMapping("/{id}/like")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> likePost(@PathVariable Long id, @AuthenticationPrincipal RegisteredUser loggedInUser) {
        Optional<Post> postOptional = postService.findById(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = postOptional.get();

        // Proveri da li je korisnik već lajkovao objavu
        if (!post.getLikedByUsers().contains(loggedInUser)) {
            post.addLikedUser(loggedInUser); // Dodaje korisnika u listu onih koji su lajkovali i ažurira countLikes
            postService.save(post); // Sačuvaj ažuriranu objavu
            return ResponseEntity.ok("Post liked successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User has already liked this post");
    }*/

   /* @PutMapping("/{id}/like")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> likeOrUnlikePost(@PathVariable Long id, @AuthenticationPrincipal RegisteredUser loggedInUser) {
        Optional<Post> postOptional = postService.findById(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = postOptional.get();

        // Proveri da li je korisnik već lajkovao objavu
        Like existingLike = post.getLikes().stream()
                .filter(like -> like.getUser().equals(loggedInUser))
                .findFirst()
                .orElse(null);

        if (existingLike != null) {
            // Ako je lajkovao, ukloni lajkovanje
            post.removeLike(existingLike);  // Ukloni lajkovanje
            postService.save(post);  // Sačuvaj ažuriranu objavu
            return ResponseEntity.ok("Like removed successfully");
        } else {
            // Ako nije lajkovao, dodaj lajkovanje
            Like like = new Like(post, loggedInUser);  // Kreiraj novi "Like"

            // Postavi korisnički ID koristeći username iz ulogovanog korisnika
            like.setUserId(loggedInUser.getId());  // Setuj userId prema loggedInUser

            post.addLike(like);  // Dodaj novi "Like"
            postService.save(post);  // Sačuvaj ažuriranu objavu
            return ResponseEntity.ok("Post liked successfully");
        }
    }*/

    @PutMapping("/{id}/like")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> likeOrUnlikePost(@PathVariable Long id) {
        // Pronađi post prema id
        Optional<Post> postOptional = postService.findById(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = postOptional.get();

        // Dohvati korisničko ime iz SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Pronađi korisnika na osnovu korisničkog imena
        Optional<RegisteredUser> loggedInUserOptional = userService.findByUsername1(username);

        if (loggedInUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        RegisteredUser loggedInUser = loggedInUserOptional.get();

        // Proveri da li je korisnik već lajkovao objavu
        Like existingLike = post.getLikes().stream()
                .filter(like -> like.getUser().equals(loggedInUser))
                .findFirst()
                .orElse(null);

        if (existingLike != null) {
            // Ako je lajkovao, ukloni lajkovanje
            post.removeLike(existingLike);  // Ukloni lajkovanje
            postService.save(post);  // Sačuvaj ažuriranu objavu
            return ResponseEntity.ok("Like removed successfully");
        } else {
            // Ako nije lajkovao, dodaj lajkovanje
            Like like = new Like(post, loggedInUser);  // Kreiraj novi "Like"
            post.addLike(like);  // Dodaj novi "Like"
            postService.save(post);  // Sačuvaj ažuriranu objavu
            return ResponseEntity.ok("Post liked successfully");
        }
    }










    // Like a post by a user
    /*@PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId, @RequestBody Long userId) {
        RegisteredUser user = userService.getUserById(userId);
        postService.likePost(postId, user);
    }*/






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




   /* @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id, @AuthenticationPrincipal RegisteredUser user) {
        postService.likePost(id, user); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok().build();
    }*/

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long id, @RequestBody Comment comment, @AuthenticationPrincipal RegisteredUser user) {
        comment.setUser(user);
        comment.setPost(postService.findById(id).orElse(null)); // Setuj post za komentar
        Comment savedComment = commentService.save(comment); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok(savedComment);
    }
}

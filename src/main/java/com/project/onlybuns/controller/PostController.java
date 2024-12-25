package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;

import com.project.onlybuns.DTO.PostDTO;

import com.project.onlybuns.model.*;
import com.project.onlybuns.repository.RegisteredUserRepository;
import com.project.onlybuns.service.ImageUploadService;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    private  final RegisteredUserRepository registeredUserRepository;


    private final UserService userService;

    private final ImageUploadService imageUploadService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService,ImageUploadService imageUploadService, RegisteredUserRepository registeredUserRepository) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
        this.registeredUserRepository = registeredUserRepository;
    }

    public String getUsernameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Dobijamo korisničko ime iz JWT tokena
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('REGISTERED')")  // Ova anotacija osigurava da samo REGISTERED korisnici mogu obrisati objavu
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        // Dobijanje korisničkog imena iz tokena
        String username = getUsernameFromToken();

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated"); // 401 if user is not authenticated
        }

        // Pronađi post po ID-u
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Proveri da li je korisnik autor objave
            if (post.getUser().getUsername().equals(username)) {
                postService.delete(id); // Obriši post
                return ResponseEntity.noContent().build(); // 204 if post was deleted successfully
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this post and cannot delete it"); // 403 if user is not the author
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found"); // 404 if post doesn't exist
        }
    }


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

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("src/main/resources/static.images/" + imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Post> createPost(
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("location") String location,
            @RequestPart("imageFile") MultipartFile imageFile) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String imageUrl;
        try {
            imageUrl = imageUploadService.uploadImage(imageFile);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        Post post = new Post();
        post.setDescription(description);
        post.setImageUrl(imageUrl);
        post.setUser(loggedUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setLatitude(latitude);
        post.setLongitude(longitude);
        post.setLocation(location);

        Post savedPost = postService.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (!post.getUser().equals(loggedUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this post and cannot update it");
            }

            if (updatedPost.getImageUrl() != null) {
                post.setImageUrl(updatedPost.getImageUrl());
            }
            if (updatedPost.getDescription() != null) {
                post.setDescription(updatedPost.getDescription());
            }

            postService.update(post);

            return ResponseEntity.ok("Post updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }



    /*@GetMapping("/allPosts")
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
    }*/

    @GetMapping("/followedPosts")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<Map<String, Object>> getFollowedUsersPosts(Authentication authentication) {
        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Izvlačenje ID-ova praćenih korisnika
        Set<Long> followedUserIds = loggedInUser.getFollowing().stream()
                .map(follow -> follow.getFollowed().getId())
                .collect(Collectors.toSet());

        // Filtriranje postova praćenih korisnika
        List<Post> followedUserPosts = postService.findAllActivePosts().stream()
                .filter(post -> post.getUser() != null && followedUserIds.contains(post.getUser().getId()))
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // Kreiranje liste postova sa detaljima
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();
        for (Post post : followedUserPosts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown");
            postData.put("countLikes", post.getLikesCount());
            String formattedDate = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "Unknown date";
            postData.put("createdAt", formattedDate);

            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown");
                String formattedCommentDate = comment.getCreatedAt() != null ? comment.getCreatedAt().format(formatter) : "Unknown date";
                commentData.put("createdAt", formattedCommentDate);
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }

    @GetMapping("/stats")
    public Map<String, Long> getPostStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("weekly", postService.countPostsForWeek());
        stats.put("monthly", postService.countPostsForMonth());
        stats.put("yearly", postService.countPostsForYear());
        return stats;
    }







    @GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts(@AuthenticationPrincipal UserProfile userDetails) {
        // Dobavljanje svih objava
        List<Post> allPosts = postService.findAllActivePosts();

        // Lista koja će sadržati sortirane objave
        List<Post> sortedPosts;

        // Provera da li je korisnik ulogovan
        if (userDetails != null) {
            // Ako je korisnik ulogovan, dobavljamo informacije o korisniku
            String loggedInUsername = userDetails.getUsername();
            RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

            // Dobavljanje ID-ova korisnika koje ulogovani korisnik prati
            Set<Long> followedUserIds = loggedInUser.getFollowing().stream()
                    .map(follow -> follow.getFollowed().getId()) // Pristup ID-u praćenog korisnika
                    .collect(Collectors.toSet());

            // Razdvajanje objava: prvo od korisnika koje korisnik prati, zatim ostale
            List<Post> followedUserPosts = allPosts.stream()
                    .filter(post -> post.getUser() != null && followedUserIds.contains(post.getUser().getId()))
                    .collect(Collectors.toList());

            List<Post> otherPosts = allPosts.stream()
                    .filter(post -> post.getUser() == null || !followedUserIds.contains(post.getUser().getId()))
                    .collect(Collectors.toList());

            // Kombinovanje objava
            sortedPosts = new ArrayList<>();
            sortedPosts.addAll(followedUserPosts); // Postovi korisnika koje korisnik prati
            sortedPosts.addAll(otherPosts);        // Postovi ostalih korisnika
        } else {
            // Ako korisnik nije ulogovan, postovi se ne sortiraju
            sortedPosts = allPosts;
        }

        // Format za datum i vreme
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // Priprema rezultata za front-end
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();
        for (Post post : sortedPosts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown");
            postData.put("countLikes", post.getLikesCount());
            String formattedDate = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "Unknown date";
            postData.put("createdAt", formattedDate);

            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown");
                String formattedCommentDate = comment.getCreatedAt() != null ? comment.getCreatedAt().format(formatter) : "Unknown date";
                commentData.put("createdAt", formattedCommentDate);
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }




    @PutMapping("/{id}/like")
    @PreAuthorize("hasRole('REGISTERED')")
    @Transactional
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

    @GetMapping("/totalPosts")
    public ResponseEntity<Map<String, Integer>> getTotalPostsCount() {
        // Dohvatanje svih registrovanih korisnika

        // Izračunavanje ukupnog broja objava
        int totalPosts = postService.findAll().size();

        // Priprema odgovora
        Map<String, Integer> response = new HashMap<>();
        response.put("totalPosts", totalPosts);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/postsLastMonth")
    public ResponseEntity<Map<String, Integer>> getPostsLastMonthCount() {
        System.out.println("Endpoint '/postsLastMonth' called."); // Provera poziva endpointa

        // Dohvatanje svih objava iz servisa
        List<Post> postsLastMonth = postService.findPostsFromLastMonth();
        System.out.println("Number of posts retrieved from service: " + postsLastMonth.size());

        // Debug: Ispisivanje datuma objava
        postsLastMonth.forEach(post ->
                System.out.println("Post ID: " + post.getId() + ", Created At: " + post.getCreatedAt())
        );

        // Izračunavanje broja objava u poslednjih mesec dana
        int totalPostsLastMonth = postsLastMonth.size();
        System.out.println("Total posts in the last month: " + totalPostsLastMonth);

        // Priprema odgovora
        Map<String, Integer> response = new HashMap<>();
        response.put("totalPostsLastMonth", totalPostsLastMonth);

        System.out.println("Response prepared: " + response);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/top5PostsLast7Days")
    public ResponseEntity<Map<String, Object>> getTop5PostsLast7Days() {
        try {
            List<Post> posts = postService.findTop5MostLikedPostsFromLast7Days();
            return ResponseEntity.ok(Map.of("top5PostsLast7Days", posts));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/top10MostLikedPosts")
    public ResponseEntity<Map<String, Object>> getTop10MostLikedPosts() {
        try {
            List<Post> posts = postService.findTop10MostLikedPosts();
            return ResponseEntity.ok(Map.of("top10MostLikedPosts", posts));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/locations")
    public ResponseEntity<List<PostDTO>> getPostsLocations() {
        List<PostDTO> locations = postService.getPostsLocations();
        return ResponseEntity.ok(locations);
    }

}

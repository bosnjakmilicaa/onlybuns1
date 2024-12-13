package com.project.onlybuns.controller;
import com.project.onlybuns.DTO.RegisteredUserDTO;
import com.project.onlybuns.DTO.RegisteredUserDTONadja;

import com.project.onlybuns.DTO.SimpleUserDTO;
import com.project.onlybuns.model.Post;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.RegisteredUserService;
import com.project.onlybuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;




@RestController// Base path for registered user-related endpoints
public class RegisteredUserController {

    private final RegisteredUserService registeredUserService;
    private final UserService registeredUserService1;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService, UserService registeredUserService1) {
        this.registeredUserService = registeredUserService;
        this.registeredUserService1 = registeredUserService1;

    }


    @GetMapping("/users/{username}")
    public ResponseEntity<RegisteredUser> getUserProfile(@PathVariable String username) {
        Optional<User> optionalUser = registeredUserService1.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // Dobijamo User objekat

            // Proveravamo da li je User zapravo instanca RegisteredUser
            if (user instanceof RegisteredUser) {
                RegisteredUser registeredUser = (RegisteredUser) user;  // Castujemo User u RegisteredUser
                RegisteredUser convertedUser = convertToRegisteredUser(registeredUser);  // Konvertujemo
                return ResponseEntity.ok(convertedUser);
            } else {
                // Ako korisnik nije tipa RegisteredUser, možete obraditi ovu situaciju
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private RegisteredUser convertToRegisteredUser(RegisteredUser registeredUser) {
        RegisteredUser newUser = new RegisteredUser();
        newUser.setUsername(registeredUser.getUsername());
        newUser.setFirstName(registeredUser.getFirstName());
        newUser.setLastName(registeredUser.getLastName());
        newUser.setEmail(registeredUser.getEmail());

        // Lista koja će sadržati stvarne Post objekte
        List<Post> posts = new ArrayList<>();

        // Pretvaranje podataka u stvarne Post objekte
        for (Post post : registeredUser.getPosts()) {
            Post newPost = new Post();
            newPost.setId(post.getId());
            newPost.setImageUrl(post.getImageUrl());
            newPost.setDescription(post.getDescription());
            newPost.setUser(post.getUser());
            newPost.setLikes(post.getLikes());
            newPost.setComments(post.getComments());
            newPost.setCreatedAt(post.getCreatedAt());

            posts.add(newPost);
        }

        newUser.setPosts(posts);  // Setovanje stvarnih postova
        return newUser;
    }





    @GetMapping("/{username}/followers")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<?> getUserFollowers(@PathVariable String username) {

        // Dobavljanje trenutnog korisnika iz SecurityContext-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Provera autentifikacije
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Provera da li ciljani korisnik postoji
        RegisteredUser targetUser = registeredUserService.findByUsername(username);
        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found.");
        }

        // Provera da li ulogovani korisnik prati ciljanog korisnika
        boolean isFollowing = registeredUserService.isAlreadyFollowing1(loggedInUsername, username);
        if (!isFollowing) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only view followers of users you follow.");
        }

        // Dohvatanje pratilaca ciljanog korisnika
        List<String> followers = registeredUserService.getFollowers(username)
                .stream()
                .map(RegisteredUser::getUsername) // Mapiraj na korisnička imena
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("followers", followers);
        response.put("followersCount", followers.size()); // Dodavanje broja pratilaca

        // Formiranje odgovora
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/following")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<?> getUserFollowing(@PathVariable String username) {

        // Dobavljanje trenutnog korisnika iz SecurityContext-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Provera autentifikacije
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Provera da li ciljani korisnik postoji
        RegisteredUser targetUser = registeredUserService.findByUsername(username);
        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found.");
        }

        // Provera da li ulogovani korisnik prati ciljanog korisnika
        boolean isFollowing = registeredUserService.isAlreadyFollowing1(loggedInUsername, username);
        if (!isFollowing) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only view the users followed by those you follow.");
        }

        // Dohvatanje korisnika koje ciljani korisnik prati
        List<String> following = registeredUserService.getFollowing(username)
                .stream()
                .map(RegisteredUser::getUsername) // Mapiraj na korisnička imena
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("following", following);
        response.put("followingCount", following.size()); // Dodavanje broja pratilaca

        // Formiranje odgovora
        return ResponseEntity.ok(following);
    }




    @PostMapping("/{followerUsername}/follow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> followUserByUsername(
            @PathVariable String followerUsername,
            @PathVariable String followedUsername) {

        // Dobavljanje trenutnog korisnika iz SecurityContext-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Provera autentifikacije
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Provera da li ulogovani korisnik pokušava da prati u svoje ime
        if (!followerUsername.equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot follow as another user.");
        }
        if (followerUsername.equals(followedUsername)) {
            return ResponseEntity.badRequest().body("You cannot follow yourself.");
        }

        // Dobavljanje ID-a ulogovanog korisnika i korisnika za praćenje
        Long followerId = registeredUserService.findUserIdByUsername(followerUsername);
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followedId == null) {
            return ResponseEntity.badRequest().body("User to follow not found.");
        }

        // Provera da li već prati korisnika
        if (registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Already following this user.");
        }

        // Praćenje korisnika
        registeredUserService.followUser(followerId, followedId);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    /*@DeleteMapping("/{followerUsername}/unfollow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> unfollowUserByUsername(
            @PathVariable String followerUsername,
            @PathVariable String followedUsername) {

        // Dobavljanje trenutnog korisnika iz SecurityContext-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Provera autentifikacije
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Provera da li ulogovani korisnik pokušava da otprati u svoje ime
        if (!followerUsername.equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot unfollow as another user.");
        }

        if (followerUsername.equals(followedUsername)) {
            return ResponseEntity.badRequest().body("You cannot unfollow yourself.");
        }

        // Dobavljanje ID-a ulogovanog korisnika i korisnika za otpraćivanje
        Long followerId = registeredUserService.findUserIdByUsername(followerUsername);
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followedId == null) {
            return ResponseEntity.badRequest().body("User to unfollow not found.");
        }

        // Provera da li korisnik prati drugog korisnika
        if (!registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Not following this user.");
        }

        // Otpraćivanje korisnika
        registeredUserService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }*/

    @DeleteMapping("/{followerUsername}/unfollow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> unfollowUserByUsername(
            @PathVariable String followerUsername,
            @PathVariable String followedUsername) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInUsername = authentication.getName();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            if (!followerUsername.equals(loggedInUsername)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot unfollow as another user.");
            }

            if (followerUsername.equals(followedUsername)) {
                return ResponseEntity.badRequest().body("You cannot unfollow yourself.");
            }

            Long followerId = registeredUserService.findUserIdByUsername(followerUsername);
            Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

            if (followedId == null) {
                return ResponseEntity.badRequest().body("User to unfollow not found.");
            }

            if (!registeredUserService.isAlreadyFollowing(followerId, followedId)) {
                return ResponseEntity.badRequest().body("Not following this user.");
            }

            registeredUserService.unfollowUser(followerId, followedId);
            return ResponseEntity.ok("Successfully unfollowed the user.");

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



    @GetMapping("/allRegisteredUsers")
    public ResponseEntity<Page<RegisteredUserDTO>> getRegisteredUsers1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dohvati korisnike koristeći paginaciju
        Page<RegisteredUser> usersPage = registeredUserService.findAll(PageRequest.of(page, size));



        // Mapiranje na DTO
        Page<RegisteredUserDTO> dtoPage = usersPage.map(user -> new RegisteredUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPosts().size(),
                user.getFollowing().size(),
                user.getFollowers().size()

        ));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/registered-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegisteredUserDTO>> getRegisteredUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dohvati korisnike koristeći paginaciju
        Page<RegisteredUser> usersPage = registeredUserService.findAll(PageRequest.of(page, size));

        // Mapiranje na DTO
        Page<RegisteredUserDTO> dtoPage = usersPage.map(user -> new RegisteredUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPosts().size(),
                user.getFollowing().size(),
                user.getFollowers().size()
        ));

        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/searchReg")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegisteredUserDTO>> searchRegisteredUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(defaultValue = "email") String sortBy, // Default sortiranje
            @RequestParam(defaultValue = "asc") String sortOrder, // Default redosled
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        // Dohvatanje svih korisnika (ovo možeš povezati sa repozitorijumom)
        List<RegisteredUser> users = registeredUserService.findAll();

        // Filtriranje sa contains pretragom
        if (firstName != null && !firstName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (lastName != null && !lastName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (email != null && !email.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (minPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() >= minPosts)
                    .collect(Collectors.toList());
        }
        if (maxPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() <= maxPosts)
                    .collect(Collectors.toList());
        }

        // Sortiranje
        Comparator<RegisteredUser> comparator;
        if (sortBy.equals("email")) {
            comparator = Comparator.comparing(RegisteredUser::getEmail, String.CASE_INSENSITIVE_ORDER);
        } else if (sortBy.equals("followingCount")) {
            comparator = Comparator.comparingInt(user -> user.getFollowing().size());
        } else {
            // Default sortiranje po email-u ako je sortBy nevalidan
            comparator = Comparator.comparing(RegisteredUser::getEmail, String.CASE_INSENSITIVE_ORDER);
        }

        if (sortOrder.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        users.sort(comparator);

        // Paginacija
        int start = Math.min(page * size, users.size());
        int end = Math.min((page + 1) * size, users.size());
        List<RegisteredUserDTO> pagedUsers = users.subList(start, end).stream()
                .map(user -> new RegisteredUserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosts().size(),
                        user.getFollowers().size(),
                        user.getFollowing().size()))
                .collect(Collectors.toList());

        // Kreiranje PageImpl objekta
        Page<RegisteredUserDTO> pageResult = new PageImpl<>(pagedUsers, PageRequest.of(page, size), users.size());

        return ResponseEntity.ok(pageResult);
    }


    @GetMapping("/all")
    public ResponseEntity<List<RegisteredUser>> getAllUsers() {
        List<RegisteredUser> users = registeredUserService.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUserById(@PathVariable Long id) {
        return registeredUserService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) {
        RegisteredUser createdUser = registeredUserService.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        registeredUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{nadjiki}")
    public ResponseEntity<RegisteredUserDTONadja> getUserProfile2(
            @PathVariable String nadjiki
            //,
           // @RequestHeader("Authorization") String authorizationHeader
            ){
        // Debugging: print the received username
        System.out.println("Fetching profile for username: " + nadjiki);

        // Look up the user by username
        RegisteredUser user = registeredUserService.findByUsername(nadjiki);
        if (user == null) {
            // User not found, return a 404
            System.out.println("User not found: " + nadjiki);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


        List<RegisteredUser> following = registeredUserService.getFollowingMicaKurva(nadjiki);
        List<RegisteredUser> followers = registeredUserService.getFollowersMicaProstakusa(nadjiki);

        // Ispisivanje rezultata u konzolu
        System.out.println("Following for " + nadjiki + ":");
        following.forEach(f -> System.out.println("- " + f.getUsername()));

        System.out.println("Followers for " + nadjiki + ":");
        followers.forEach(f -> System.out.println("- " + f.getUsername()));




        RegisteredUserDTONadja userDTO = new RegisteredUserDTONadja(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getAddress(),
                user.getFollowersCount(),
                user.getFollowingCount(),
                following.stream()
                        .map(f -> new SimpleUserDTO(f.getUsername()))
                        .collect(Collectors.toList()),
                followers.stream()
                        .map(f -> new SimpleUserDTO(f.getUsername()))
                        .collect(Collectors.toList())
        );




        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/totalPosts")
    @PreAuthorize("hasRole('ADMIN')") // Možete ukloniti ovu liniju ako želite da svi korisnici vide podatke
    public ResponseEntity<Map<String, Object>> getTotalPostsCount() {
        // Dohvatanje svih registrovanih korisnika
        List<RegisteredUser> users = registeredUserService.findAll();

        // Izračunavanje ukupnog broja objava
        int totalPosts = users.stream()
                .mapToInt(user -> user.getPosts().size()) // Pretpostavlja se da korisnici imaju listu "posts"
                .sum();

        // Priprema odgovora
        Map<String, Object> response = new HashMap<>();
        response.put("totalPosts", totalPosts);

        return ResponseEntity.ok(response);
    }








}

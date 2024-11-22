package com.project.onlybuns.controller;
import com.project.onlybuns.DTO.RegisteredUserDTO;
import com.project.onlybuns.model.RegisteredUser;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RestController// Base path for registered user-related endpoints
public class RegisteredUserController {

    private final RegisteredUserService registeredUserService;
    private final UserService registeredUserService1;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService,UserService registeredUserService1) {
        this.registeredUserService = registeredUserService;
        this.registeredUserService1 = registeredUserService1;

    }
    /*@PostMapping("/{followerId}/follow/{followedId}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> followUser(
            @PathVariable Long followerId,
            @PathVariable Long followedId) {
        if (registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Already following this user.");
        }
        registeredUserService.followUser(followerId, followedId);
        return ResponseEntity.ok("Successfully followed the user.");
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> unfollowUser(
            @PathVariable Long followerId,
            @PathVariable Long followedId) {
        if (!registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Not following this user.");
        }
        registeredUserService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }*/

    /*@PostMapping("/{followerUsername}/follow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> followUserByUsername(
            @PathVariable String followerUsername,
            @PathVariable String followedUsername) {

        Long followerId = registeredUserService.findUserIdByUsername(followerUsername);
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followerId == null || followedId == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        if (registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Already following this user.");
        }

        registeredUserService.followUser(followerId, followedId);
        return ResponseEntity.ok("Successfully followed the user.");
    }


    @GetMapping("/me")
    public ResponseEntity<RegisteredUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        // Dobijanje trenutnog korisnika sa SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Vaša logika da dobijete korisnika na osnovu username-a
        RegisteredUser currentUser = registeredUserService.getCurrentUserByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(currentUser);
    }


    @DeleteMapping("/{followerUsername}/unfollow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> unfollowUserByUsername(
            @PathVariable String followerUsername,
            @PathVariable String followedUsername) {

        Long followerId = registeredUserService.findUserIdByUsername(followerUsername);
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followerId == null || followedId == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        if (!registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Not following this user.");
        }

        registeredUserService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }*/

    /*@PostMapping("/{followerId}/follow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> followUserByUsername(
            @PathVariable Long followerId,
            @PathVariable String followedUsername) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Provera da li je korisnik ulogovan i da li ima ulogu 'REGISTERED'
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followerId == null || followedId == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        if (registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Already following this user.");
        }

        registeredUserService.followUser(followerId, followedId);
        return ResponseEntity.ok("Successfully followed the user.");
    }*/
    @PostMapping("/{followerId}/follow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> followUserByUsername(
            @PathVariable Long followerId,
            @PathVariable String followedUsername) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Trenutni username

        System.out.println("User: " + username + " with roles: " + authentication.getAuthorities());

        // Provera da li je korisnik ulogovan i da li ima ulogu 'REGISTERED'
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Dobijanje ID-a korisnika na osnovu username-a
        Long loggedInUserId = registeredUserService.findUserIdByUsername(username);

        // Ako ID-u ulogovanog korisnika ne odgovara followerId, vraćamo grešku
        if (!followerId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot follow as another user.");
        }

        // Dobijanje ID-a korisnika kojeg želimo da pratimo
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followedId == null) {
            return ResponseEntity.badRequest().body("User to follow not found.");
        }

        if (registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Already following this user.");
        }

        registeredUserService.followUser(followerId, followedId);
        return ResponseEntity.ok("Successfully followed the user.");
    }



    @DeleteMapping("/{followerId}/unfollow/{followedUsername}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> unfollowUserByUsername(
            @PathVariable Long followerId,
            @PathVariable String followedUsername) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Trenutni username

        // Provera da li je korisnik ulogovan i da li ima ulogu 'REGISTERED'
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Dobijanje ID-a korisnika na osnovu username-a
        Long loggedInUserId = registeredUserService.findUserIdByUsername(username);

        // Ako ID ulogovanog korisnika ne odgovara followerId, vraćamo grešku
        if (!followerId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot unfollow as another user.");
        }

        // Dobijanje ID-a korisnika kojeg želimo da otpratimo
        Long followedId = registeredUserService.findUserIdByUsername(followedUsername);

        if (followedId == null) {
            return ResponseEntity.badRequest().body("User to unfollow not found.");
        }

        if (!registeredUserService.isAlreadyFollowing(followerId, followedId)) {
            return ResponseEntity.badRequest().body("Not following this user.");
        }

        registeredUserService.unfollowUser(followerId, followedId);
        return ResponseEntity.ok("Successfully unfollowed the user.");
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<RegisteredUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        // Get the username from the SecurityContext (after the token has been parsed by the filter)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Get the current user based on the username
        RegisteredUser currentUser = registeredUserService.getCurrentUserByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(currentUser);
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
}

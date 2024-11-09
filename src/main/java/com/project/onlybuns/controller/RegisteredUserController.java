package com.project.onlybuns.controller;
import com.project.onlybuns.DTO.RegisteredUserDTO;
import com.project.onlybuns.service.UserService;
import org.springframework.security.core.Authentication;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.RegisteredUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    /*@GetMapping("/registered-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredUser>> getRegisteredUsers() {
        // Logovanje za debugging
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dobijanje registrovanih korisnika
        List<RegisteredUser> registeredUsers = registeredUserService.findAll();
        return ResponseEntity.ok(registeredUsers);
    }*/

    @GetMapping("/registered-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredUserDTO>> getRegisteredUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Mapiramo RegisteredUser na RegisteredUserDTO
        List<RegisteredUserDTO> registeredUsers = registeredUserService.findAll()
                .stream()
                .map(user -> new RegisteredUserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosts().size(),
                        user.getFollowersCount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(registeredUsers);
    }


    /*@GetMapping("/searchReg")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegisteredUser>> searchRegisteredUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) Boolean sortByFollowers) {

        List<RegisteredUser> users = registeredUserService1.searchRegisteredUsers(firstName, lastName, email, minPosts, maxPosts, sortByFollowers);
        return ResponseEntity.ok(users);
    }*/

    @GetMapping("/searchReg")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegisteredUserDTO> searchRegisteredUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) Boolean sortByFollowers) {

        // Dohvatanje svih korisnika
        List<RegisteredUser> users = registeredUserService.findAll();

        // Filtriranje po imenu ako je zadato
        if (firstName != null && !firstName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getFirstName().equalsIgnoreCase(firstName))
                    .collect(Collectors.toList());
        }

        // Filtriranje po prezimenu ako je zadato
        if (lastName != null && !lastName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getLastName().equalsIgnoreCase(lastName))
                    .collect(Collectors.toList());
        }

        // Filtriranje po email-u ako je zadato
        if (email != null && !email.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
                    .collect(Collectors.toList());
        }

        // Filtriranje po broju postova između minPosts i maxPosts, ako su oba parametra zadata
        if (minPosts != null && maxPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() >= minPosts && user.getPosts().size() <= maxPosts)
                    .collect(Collectors.toList());
        } else if (minPosts != null) {
            // Filtriranje po minimalnom broju postova ako je samo minPosts zadat
            users = users.stream()
                    .filter(user -> user.getPosts().size() >= minPosts)
                    .collect(Collectors.toList());
        } else if (maxPosts != null) {
            // Filtriranje po maksimalnom broju postova ako je samo maxPosts zadat
            users = users.stream()
                    .filter(user -> user.getPosts().size() <= maxPosts)
                    .collect(Collectors.toList());
        }

        // Sortiranje po broju pratilaca ako je zadato
        if (Boolean.TRUE.equals(sortByFollowers)) {
            users.sort(Comparator.comparingInt(RegisteredUser::getFollowersCount).reversed());
        }

        // Mapiranje korisnika na DTO
        return users.stream()
                .map(user -> new RegisteredUserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPosts().size(),
                        user.getFollowersCount()))
                .collect(Collectors.toList());
    }






    /*@GetMapping("/registered-users")
    public ResponseEntity<?> getAllRegisteredUsers(HttpSession session) {
        // Proveri da li je ulogovani korisnik administrator
        Object userType = session.getAttribute("userType");
        if (userType != null && userType.equals("ADMIN")) {
            List<RegisteredUser> registeredUsers = registeredUserService.findAll();
            return ResponseEntity.ok(registeredUsers);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("message", "Error: You do not have permission to access this resource."));
        }
    }*/




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

package com.project.onlybuns.controller;
import com.project.onlybuns.DTO.RegisteredUserDTO;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.RegisteredUserService;
import com.project.onlybuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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


    /*@GetMapping("/registered-users")
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
                        user.getFollowing().size(),
                        user.getFollowersCount()))  // Dodajte ovde šesti parametar
                .collect(Collectors.toList());


        return ResponseEntity.ok(registeredUsers);
    }*/

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


    /*@GetMapping("/searchReg")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegisteredUserDTO>> searchRegisteredUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer minPosts,
            @RequestParam(required = false) Integer maxPosts,
            @RequestParam(required = false) String sortBy, // Koristimo String za sortiranje (email ili followingCount)
            @RequestParam(required = false) String sortOrder, // Dodajemo parametar za redosled (asc ili desc)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        // Dohvatanje svih korisnika
        List<RegisteredUser> users = registeredUserService.findAll();

        // Filtriranje korisnika po parametrima (isto kao ranije)
        if (firstName != null && !firstName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getFirstName().equalsIgnoreCase(firstName))
                    .collect(Collectors.toList());
        }
        if (lastName != null && !lastName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getLastName().equalsIgnoreCase(lastName))
                    .collect(Collectors.toList());
        }
        if (email != null && !email.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
                    .collect(Collectors.toList());
        }
        if (minPosts != null && maxPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() >= minPosts && user.getPosts().size() <= maxPosts)
                    .collect(Collectors.toList());
        } else if (minPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() >= minPosts)
                    .collect(Collectors.toList());
        } else if (maxPosts != null) {
            users = users.stream()
                    .filter(user -> user.getPosts().size() <= maxPosts)
                    .collect(Collectors.toList());
        }

        // Sortiranje na osnovu izabranog atributa i redosleda
        Sort sort = Sort.unsorted(); // Početni sortirani objekt


        // Implementacija paginacije sa sortiranjem
        Pageable pageable = PageRequest.of(page, size, sort);

        // Pretvaranje korisnika u Page objekat sa paginacijom i sortiranjem
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

        // Kreiranje PageImpl sa paginacijom
        Page<RegisteredUserDTO> pageResult = new PageImpl<>(pagedUsers, pageable, users.size());

        return ResponseEntity.ok(pageResult);
    }*/

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

        // Filtriranje
        if (firstName != null && !firstName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getFirstName().equalsIgnoreCase(firstName))
                    .collect(Collectors.toList());
        }
        if (lastName != null && !lastName.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getLastName().equalsIgnoreCase(lastName))
                    .collect(Collectors.toList());
        }
        if (email != null && !email.isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
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

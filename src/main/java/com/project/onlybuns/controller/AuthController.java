package com.project.onlybuns.controller;

import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.AdminUser;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;



    RegisteredUser registeredUser;
    @Autowired
    private PasswordEncoder passwordEncoder;



    /*@PostMapping("/register") // POST "/auth/register"
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO userDTO, HttpSession session) {
        // Proveri da li korisničko ime već postoji
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Proveri da li e-mail već postoji
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Heširaj lozinku pre nego što je sačuvamo
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Sačuvaj korisnika u bazi podataka
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());
        userService.save(registeredUser);

        // Kreiraj sesiju za registrovanog korisnika
        session.setAttribute("user", registeredUser);
        session.setAttribute("userType", "REGISTERED");

        return ResponseEntity.ok("User registered successfully and session started!");
    }*/

    /*@PostMapping("/register") // POST "/auth/register"
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult, HttpSession session) {
        // Proveri da li postoje greške u validaciji
        if (bindingResult.hasErrors()) {
            // Kreiraj listu poruka grešaka
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // Proveri da li korisničko ime već postoji
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Username is already taken!"));
        }

        // Proveri da li e-mail već postoji
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Email is already in use!"));
        }

        // Heširaj lozinku pre nego što je sačuvamo
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Sačuvaj korisnika u bazi podataka
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());
        userService.save(registeredUser);

        // Kreiraj sesiju za registrovanog korisnika
        session.setAttribute("user", registeredUser);
        session.setAttribute("userType", "REGISTERED");

        // Pripremi odgovor
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully and session started!");

        return ResponseEntity.ok(response); // Vraća JSON objekat
    }*/

    @PostMapping("/register") // POST "/auth/register"
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult, HttpSession session) {
        // Proveri da li postoje greške u validaciji
        if (bindingResult.hasErrors()) {
            // Kreiraj listu poruka grešaka
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // Proveri da li korisničko ime već postoji
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("username", "Error: Username is already taken!"));
        }

        // Proveri da li e-mail već postoji
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Error: Email is already in use!"));
        }

        // Heširaj lozinku pre nego što je sačuvamo
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Sačuvaj korisnika u bazi podataka
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());
        userService.save(registeredUser);

        // Kreiraj sesiju za registrovanog korisnika
        session.setAttribute("user", registeredUser);
        session.setAttribute("userType", "REGISTERED");

        // Pripremi odgovor
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully and session started!");

        return ResponseEntity.ok(response); // Vraća JSON objekat
    }







    @PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, HttpSession session) {
        try {
            // Proveri da li je korisnik već ulogovan
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Error: User is already logged in!"));
            }

            String email = userInput.get("email");
            String password = userInput.get("password");

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Email must be provided!"));
            }

            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Password must be provided!"));
            }

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    String userType = existingUser instanceof AdminUser ? "ADMIN" : "REGISTERED";
                    // Kreiraj sesiju
                    session.setAttribute("user", existingUser);
                    session.setAttribute("userId", existingUser.getId());
                    session.setAttribute("userType", userType);

                    // Pripremi odgovor
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User logged in successfully!");
                    response.put("userType", userType); // Dodaj tip korisnika

                    return ResponseEntity.ok(response); // Vraća odgovor sa tipom korisnika
                } else {
                    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Invalid password!"));
                }
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: User not found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error: An unexpected error occurred."));
        }
    }



    @PostMapping("/updatePasswords")
    public ResponseEntity<?> updatePasswords() {
        updateOldPasswords();
        return ResponseEntity.ok("Old passwords updated successfully!");
    }

    public void updateOldPasswords() {
        List<User> users = userService.findAll(); // Uzimanje svih korisnika
        for (User user : users) {
            // Proveri da li je trenutna lozinka "password123"
            if (user.getPassword().equals("password123")) {
                // Dodeli novu lozinku
                String newPassword = "newPassword123"; // Postavi novu lozinku
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                userService.save(user); // Sačuvaj ažuriranog korisnika
            }
        }
    }




    @PostMapping("/logout") // POST "/auth/logout"
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate(); // Poništi sesiju
        return ResponseEntity.ok("User logged out successfully!");
    }




}

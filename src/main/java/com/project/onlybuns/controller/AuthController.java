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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;



    RegisteredUser registeredUser;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/register") // POST "/auth/register"
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
    }





    /*@PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, HttpSession session) {
        try {
            String email = userInput.get("email");
            String password = userInput.get("password");

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    if (existingUser instanceof RegisteredUser || existingUser instanceof AdminUser) {
                        // Kreiraj sesiju
                        session.setAttribute("user", existingUser);
                        session.setAttribute("userType", existingUser instanceof AdminUser ? "ADMIN" : "REGISTERED");
                        return ResponseEntity.ok("User logged in successfully!");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Error: Only registered users or admins can log in!");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Error: Invalid password!");
                }
            } else {
                return ResponseEntity.badRequest().body("Error: User not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred.");
        }
    }*/

    @PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, HttpSession session) {
        try {
            // Proveri da li je korisnik već ulogovan
            if (session.getAttribute("user") != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Error: User is already logged in!");
            }

            String email = userInput.get("email");
            String password = userInput.get("password");

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    if (existingUser instanceof RegisteredUser || existingUser instanceof AdminUser) {
                        // Kreiraj sesiju
                        session.setAttribute("user", existingUser);
                        session.setAttribute("userType", existingUser instanceof AdminUser ? "ADMIN" : "REGISTERED");
                        return ResponseEntity.ok("User logged in successfully!");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Error: Only registered users or admins can log in!");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Error: Invalid password!");
                }
            } else {
                return ResponseEntity.badRequest().body("Error: User not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred.");
        }
    }


    @PostMapping("/logout") // POST "/auth/logout"
    public ResponseEntity<?> logoutUser(HttpSession session) {
        session.invalidate(); // Poništi sesiju
        return ResponseEntity.ok("User logged out successfully!");
    }




}

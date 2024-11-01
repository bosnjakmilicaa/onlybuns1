package com.project.onlybuns.controller;

import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.AdminUser;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        // Proveri da li je korisnik tipa Unregistered
        // Možda želite dodati polje userType u UserDTO ili ga obraditi drugačije
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Proveri da li korisničko ime već postoji
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Proveri da li e-mail već postoji
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Šifruj lozinku pre nego što je sačuvamo

        // Sačuvaj korisnika u bazi podataka
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(userDTO.getPassword()); // Šifrovana lozinka
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());
        userService.save(registeredUser);

        return ResponseEntity.ok("User registered successfully!");
    }



    // Pretpostavljam da ovo radite negde pri registraciji korisnika





    @PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput) {
        try {
            // Uzmite korisničko ime i lozinku iz Map
            String username = userInput.get("username");
            String password = userInput.get("password");

            // Pronađi korisnika po korisničkom imenu
            Optional<User> optionalUser = userService.findByUsername(username);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                // Ispisivanje informacija za debagovanje
                System.out.println("Found user: " + existingUser.getUsername());
                System.out.println("Stored password: " + existingUser.getPassword());
                System.out.println("Provided password: " + password);

                // Proveri da li je lozinka tačna
                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    // Proveri tip korisnika
                    if (existingUser instanceof RegisteredUser || existingUser instanceof AdminUser) {
                        // Uspesno prijavljen
                        return ResponseEntity.ok("User logged in successfully!");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Error: Only registered users or admins can log in!");
                    }
                } else {
                    // Greška: lozinka nije tačna
                    return ResponseEntity.badRequest().body("Error: Invalid password!");
                }
            } else {
                // Greška: korisnik nije pronađen
                return ResponseEntity.badRequest().body("Error: User not found!");
            }
        } catch (Exception e) {
            // Loguj izuzetak
            e.printStackTrace(); // Ili koristi neki logger
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred.");
        }
    }




}

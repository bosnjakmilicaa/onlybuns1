package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;
import com.project.onlybuns.service.EmailService;
import com.project.onlybuns.service.LoginAttemptService;
import com.project.onlybuns.service.RegisteredUserService;
import io.jsonwebtoken.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.AdminUser;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.UserService;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    RegisteredUser registeredUser;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public AuthController(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO userDTO, BindingResult bindingResult)throws MessagingException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("username", "Error: Username is already taken!"));
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Error: Email is already in use!"));
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());

        // Korisnik je inicijalno inaktiviran
        registeredUser.setActive(false);
        userService.save(registeredUser);

        // Generiši token za aktivaciju
        String token = jwtAuthenticationFilter.generateTokenWithExpiration(registeredUser,600000);

        System.out.println("Generisani token: " + token);


        emailService.sendActivationEmail(userDTO.getEmail(), token);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully! Please check your email for activation link."));
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateUser(@RequestParam("token") String token) {
        Claims claims;
        try {
            claims = jwtAuthenticationFilter.parseToken(token);
            System.out.println("Token parsed successfully: " + claims.getSubject()); // Dodajte log za praćenje
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Token has expired"));
        } catch (JwtException e) {
            System.out.println("Invalid token");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid token"));
        }

        // Dobavljanje korisnika pomoću username-a koji je u subject-u tokena
        String username = claims.getSubject();
        RegisteredUser user = userService.findByUsername1(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        // Proverite da li je korisnik već aktiviran
        if (user.isActive()) {
            System.out.println("Account already activated for user: " + username);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Account is already activated"));
        }

        // Aktivirajte korisnika
        user.setActive(true);
        userService.save(user);

        System.out.println("User account activated: " + username);

        return ResponseEntity.ok(Collections.singletonMap("message", "Account successfully activated. You can now log in."));
    }







    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        try {
            emailService.sendSimpleEmail(to, subject, body);
            return "Email sent successfully";
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }




    @PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, @RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletRequest request) {
        try {
            // Ako već postoji Authorization header (token), vrati poruku da je korisnik već ulogovan
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
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

            // Get client IP address
            String ipAddress = request.getRemoteAddr();

            // Check if this IP has exceeded the login attempt limit
            if (loginAttemptService.isBlocked(ipAddress)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Error: Too many login attempts. Please try again later."));
            }

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();

                if (!existingUser.isActive()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Collections.singletonMap("message", "Error: Your account is not activated. Please activate your account."));
                }


                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    String userType = existingUser instanceof AdminUser ? "ADMIN" : "REGISTERED";
                    String token = jwtAuthenticationFilter.generateToken(existingUser);

                    // Reset login attempts upon successful login
                    loginAttemptService.resetAttempts(ipAddress);

                    // Prepare response
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User logged in successfully!");
                    response.put("userType", userType);
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                } else {
                    // Increment failed attempts
                    loginAttemptService.incrementAttempts(ipAddress);

                    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Invalid password!"));
                }
            } else {
                // Increment failed attempts
                loginAttemptService.incrementAttempts(ipAddress);

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

        List<RegisteredUser> users = registeredUserService.findAll();

        // Ažuriranje postsCount za svakog korisnika
        for (RegisteredUser user : users) {
            int postsCount = user.getPosts() != null ? user.getPosts().size() : 0;
            user.setPostsCount(postsCount);

            int likesCount = user.getLikes() != null ? user.getLikes().size() : 0; // Pretpostavljamo da postoji lista likes za korisnika
            user.setLikesCount(likesCount);

            int followersCount = user.getFollowers() != null ? user.getFollowers().size() : 0; // Pretpostavljamo da postoji lista likes za korisnika
            user.setFollowersCount(followersCount);

            int followingCount = user.getFollowing() != null ? user.getFollowing().size() : 0; // Pretpostavljamo da postoji lista likes za korisnika
            user.setFollowingCount(followingCount);

            registeredUserService.save(user); // Sačuvaj korisnika sa ažuriranim postsCount
        }
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



    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully!"));
    }





}

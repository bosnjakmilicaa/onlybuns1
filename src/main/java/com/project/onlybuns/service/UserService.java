package com.project.onlybuns.service;

import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.AdminUserRepository;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import com.project.onlybuns.repository.UnregisteredUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda za vraćanje svih korisnika

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        updatePasswords(); // Ova metoda će se izvršiti prilikom pokretanja aplikacije
    }

    public void updatePasswords() {
        List<User> users = userRepository.findAll(); // Uzmite sve korisnike
        for (User user : users) {
            // Proverite da li je lozinka već BCrypt
            if (!user.getPassword().startsWith("$2a$")) { // BCrypt lozinke počinju sa $2a$
                String encodedPassword = passwordEncoder.encode(user.getPassword()); // Šifrujte lozinku
                user.setPassword(encodedPassword);
                userRepository.save(user); // Sačuvajte izmenjenog korisnika
            }
        }
    }
    public void loginUser(UserDTO userDTO) {
        String passwordToSave;

        // Proverite da li je lozinka "password123"
        if ("password123".equals(userDTO.getPassword())) {
            // Haširajte lozinku
            passwordToSave = passwordEncoder.encode(userDTO.getPassword());
        } else {
            // Ako nije "password123", sačuvajte je u originalnom obliku ili kako već želite
            passwordToSave = userDTO.getPassword(); // ili bacite grešku ako ne želite da prihvatite druge lozinke
        }

        // Sada sačuvajte `passwordToSave` u bazi
        // yourRepository.save(new User(..., passwordToSave, ...));
    }
    public boolean login(String username, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Proverite da li se uneta lozinka poklapa sa šifrovanom lozinkom
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false; // Korisnik nije pronađen
    }

    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user); // Čuvanje korisnika u bazi
    }

    public List<User> findAllUsers() {
        return userRepository.findAll(); // Ova metoda bi trebala vraćati sve korisnike kao User
    }
    /*public List<RegisteredUser> findAll() {
        return userRepository.findAll();
    }*/

    /*public Optional<RegisteredUser> findById(Long id) {
        return userRepository.findById(id);
    }*/

    public RegisteredUser save(RegisteredUser user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }



    /*public RegisteredUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }*/
}

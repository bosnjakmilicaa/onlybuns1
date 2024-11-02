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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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

    public List<User> searchUsers(String name, String surname, String email) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(name, surname, email);
    }

    public List<User> findUsersByPostsCount(int min, int max) {
        return userRepository.findByPostsCountBetween(min, max);
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

    public Optional<User> findByEmail(String username) {
        return  userRepository.findByEmail(username);
    }
    public List<User> searchUsers(String firstName, String lastName, String email, Integer minPosts, Integer maxPosts) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(firstName, lastName, email);
        if (minPosts != null || maxPosts != null) {
            // Filtriranje korisnika na osnovu broja objava
            return users.stream()
                    .filter(user -> (minPosts == null || user.getPostsCount() >= minPosts) &&
                            (maxPosts == null || user.getPostsCount() <= maxPosts))
                    .toList();
        }
        return users;
    }

    public List<User> searchUsers(String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, Boolean sortByFollowers) {
        List<User> users = userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(firstName, lastName, email);

        // Filtriranje korisnika na osnovu broja objava
        if (minPosts != null || maxPosts != null) {
            users = users.stream()
                    .filter(user -> (minPosts == null || user.getPostsCount() >= minPosts) &&
                            (maxPosts == null || user.getPostsCount() <= maxPosts))
                    .toList();
        }

        // Sortiranje
        if (sortByFollowers != null) {
            users = sortByFollowersAndEmail(sortByFollowers);
        }

        return users;
    }
    
    



    public List<User> sortByFollowersAndEmail(boolean sortByFollowers) {
        List<User> users = userRepository.findAll();
        if (sortByFollowers) {
            return users.stream()
                    .sorted((u1, u2) -> Integer.compare(u2.getFollowersCount(), u1.getFollowersCount())) // Pretpostavljajući da imate metodu getFollowersCount()
                    .toList();
        } else {
            return users.stream()
                    .sorted((u1, u2) -> u1.getEmail().compareTo(u2.getEmail()))
                    .toList();
        }
    }

    public List<User> sortByFollowersAndEmail() {
        List<User> users = userRepository.findAll(); // Uzimamo sve korisnike
        users.sort((user1, user2) -> {
            int comparison = Integer.compare(user1.getFollowersCount(), user2.getFollowersCount());
            if (comparison == 0) {
                // Ako su brojevi pratilaca isti, poredi po email adresi
                return user1.getEmail().compareTo(user2.getEmail());
            }
            return comparison;
        });
        return users; // Vraćamo sortiranu listu
    }
    public void save(User user) {
        userRepository.save(user); // Čuvanje korisnika u bazi
    }

    public List<User> findAll() {
        return userRepository.findAll(); // Vraća listu svih korisnika iz baze
    }


    /*public RegisteredUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }*/
}

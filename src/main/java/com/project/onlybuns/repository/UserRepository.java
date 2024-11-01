package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //RegisteredUser findByUsername(String username);  // Metoda za pronalaženje korisnika po korisničkom imenu
    boolean existsByUsername(String username); // Proverava da li korisničko ime već postoji
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    //RegisteredUser findByUsername(String username);
    Optional<RegisteredUser> findRegisteredUserByUsername(String username); // Vraća Optional<RegisteredUser>

}

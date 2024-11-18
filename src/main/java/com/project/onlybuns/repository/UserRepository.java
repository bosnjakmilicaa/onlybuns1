package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //RegisteredUser findByUsername(String username);  // Metoda za pronalaženje korisnika po korisničkom imenu
    boolean existsByUsername(String username); // Proverava da li korisničko ime već postoji
    boolean existsByEmail(String email);

    List<User> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email);
    List<User> findByPostsCountBetween(int min, int max);

    void deleteByIsActiveFalse();
    void deleteByIsActiveFalseAndRegistrationDateBefore(LocalDateTime dateTime);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    //RegisteredUser findByUsername(String username);
    Optional<RegisteredUser> findRegisteredUserByUsername(String username); // Vraća Optional<RegisteredUser>

}

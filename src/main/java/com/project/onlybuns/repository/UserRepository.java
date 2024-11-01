package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //RegisteredUser findByUsername(String username);  // Metoda za pronalaženje korisnika po korisničkom imenu
}

package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    // Dodatne metode za pretragu mogu se definisati ovde

        List<RegisteredUser> findByFirstNameContainingOrLastNameContainingOrEmailContaining(String firstName, String lastName, String email);
        List<RegisteredUser> findByPostsCountBetween(int min, int max);


}

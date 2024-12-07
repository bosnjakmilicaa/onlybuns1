package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> findByUsername(String username);
    @Query("SELECT u FROM RegisteredUser u WHERE u.username = :username")
    RegisteredUser findByUsername2(@Param("username") String username);
    List<RegisteredUser> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email);

    List<RegisteredUser> findByPostsCountBetween(int min, int max);

    // Dodavanje paginacije
    Page<RegisteredUser> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email, Pageable pageable);

    Page<RegisteredUser> findByPostsCountBetween(int min, int max, Pageable pageable);


}

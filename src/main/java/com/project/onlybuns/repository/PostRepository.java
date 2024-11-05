package com.project.onlybuns.repository;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll();  // Pronalazak svih objava
    List<Post> findByIsDeletedFalse();
    List<Post> findByUserUsername(String username);

    List<Post> findByUser(RegisteredUser user); // Preporučeno

}

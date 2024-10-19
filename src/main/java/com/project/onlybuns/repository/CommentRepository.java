package com.project.onlybuns.repository;

import com.project.onlybuns.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Možeš dodati specifične metode za upite ako je potrebno
}

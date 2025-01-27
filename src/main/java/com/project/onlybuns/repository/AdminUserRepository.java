package com.project.onlybuns.repository;

import com.project.onlybuns.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    // Dodaj metode za specifične upite ako je potrebno
    boolean existsByEmail(String email);
}

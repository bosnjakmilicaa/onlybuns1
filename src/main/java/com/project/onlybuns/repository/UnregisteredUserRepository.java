package com.project.onlybuns.repository;

import com.project.onlybuns.model.UnregisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnregisteredUserRepository extends JpaRepository<UnregisteredUser, Long> {
    // Dodatne metode za pretragu mogu se dodati ovde
}

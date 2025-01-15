package com.project.onlybuns.repository;

import com.project.onlybuns.model.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    // Pronalaženje grupnog četa po ID-u admina
    List<ChatGroup> findByAdminId(Long adminId);

    // Pronalaženje svih grupa kojima je određeni korisnik član
    List<ChatGroup> findByMembersId(Long userId);


    Optional<ChatGroup> findByName(String name);
}

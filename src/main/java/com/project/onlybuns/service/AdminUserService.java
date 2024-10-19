package com.project.onlybuns.service;

import com.project.onlybuns.model.AdminUser;
import com.project.onlybuns.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    @Autowired
    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public List<AdminUser> findAll() {
        return adminUserRepository.findAll();
    }

    public Optional<AdminUser> findById(Long id) {
        return adminUserRepository.findById(id);
    }

    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    public void delete(Long id) {
        adminUserRepository.deleteById(id);
    }
}

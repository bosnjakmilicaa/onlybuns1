package com.project.onlybuns.service;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;

    @Autowired
    public RegisteredUserService(RegisteredUserRepository registeredUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
    }

    public List<RegisteredUser> findAll() {
        return registeredUserRepository.findAll();
    }

    public Optional<RegisteredUser> findById(Long id) {
        return registeredUserRepository.findById(id);
    }

    public RegisteredUser save(RegisteredUser user) {
        return registeredUserRepository.save(user);
    }

    public void delete(Long id) {
        registeredUserRepository.deleteById(id);
    }
}

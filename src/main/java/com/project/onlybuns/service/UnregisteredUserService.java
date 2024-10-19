package com.project.onlybuns.service;

import com.project.onlybuns.model.UnregisteredUser;
import com.project.onlybuns.repository.UnregisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnregisteredUserService {

    private final UnregisteredUserRepository unregisteredUserRepository;

    @Autowired
    public UnregisteredUserService(UnregisteredUserRepository unregisteredUserRepository) {
        this.unregisteredUserRepository = unregisteredUserRepository;
    }

    public List<UnregisteredUser> findAll() {
        return unregisteredUserRepository.findAll();
    }

    public Optional<UnregisteredUser> findById(Long id) {
        return unregisteredUserRepository.findById(id);
    }

    public UnregisteredUser save(UnregisteredUser user) {
        return unregisteredUserRepository.save(user);
    }

    public void delete(Long id) {
        unregisteredUserRepository.deleteById(id);
    }
}

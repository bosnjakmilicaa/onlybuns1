package com.project.onlybuns.service;

import com.project.onlybuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserCleanupService {

    @Autowired
    private UserRepository userRepository;

    // Metoda koja se poziva poslednjeg dana svakog meseca
    @Scheduled(cron = "0 0 0 28-31 * ?")  // Ovaj cron izraz omogućava pozivanje zadatka poslednjeg dana u mesecu
    public void deleteInactiveUsers() {
        // Brisanje svih korisnika koji nisu aktivirani (isActive == false)
        userRepository.deleteByIsActiveFalse();
    }
}
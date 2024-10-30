package com.project.onlybuns.service;

import com.project.onlybuns.model.UnregisteredUser;
import com.project.onlybuns.repository.UnregisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UnregisteredUserService {

    private final UnregisteredUserRepository unregisteredUserRepository;
    private final JavaMailSender mailSender; // Dodaj ovo

    @Autowired
    public UnregisteredUserService(UnregisteredUserRepository unregisteredUserRepository, JavaMailSender mailSender) {
        this.unregisteredUserRepository = unregisteredUserRepository;
        this.mailSender = mailSender; // Inicijalizuj mailSender
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

    // Metoda za registraciju korisnika
    public void registerUser(UnregisteredUser user) {
        // Proveri validnost unosa
        if (!user.validateRegistration()) {
            throw new IllegalArgumentException("Nevalidni podaci za registraciju.");
        }

        // Sačuvaj korisnika u bazi
        user.setActive(false); // postavi isActive na false
        unregisteredUserRepository.save(user);

        // Generiši jedinstveni aktivacioni token
        String token = UUID.randomUUID().toString(); // Generiši token

        // Pošalji email sa linkom za aktivaciju
        sendActivationEmail(user.getEmail(), token);
    }

    // Metoda za slanje email-a
    private void sendActivationEmail(String email, String token) {
        String activationLink = "http://localhost:8080/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Aktivacija naloga");
        message.setText("Da aktivirate svoj nalog, kliknite na sledeći link: " + activationLink);
        mailSender.send(message);
    }
}

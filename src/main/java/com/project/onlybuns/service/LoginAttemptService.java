package com.project.onlybuns.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    // Maksimalan broj pokušaja
    private final int MAX_ATTEMPTS = 5;

    // Vreme nakon kog se broj pokušaja resetuje (1 minuta)
    private final long ATTEMPT_RESET_TIME = 1; // u minutama

    // Mapa koja čuva informacije o pokušajima prijave po IP adresama
    private ConcurrentHashMap<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    // Metod za proveru da li je IP blokiran zbog previše pokušaja
    public boolean isBlocked(String ip) {
        LoginAttempt attempt = attemptsCache.get(ip);

        if (attempt == null) {
            return false;  // Ako ne postoji zapis o IP adresi, znači nije blokiran
        }

        // Proveravamo da li je prošlo više od definisanog vremena (1 minut)
        if (System.currentTimeMillis() - attempt.getLastAttempt() > TimeUnit.MINUTES.toMillis(ATTEMPT_RESET_TIME)) {
            resetAttempts(ip);  // Ako je prošlo više vremena, resetujemo pokušaje
            return false;
        }

        // Ako je broj pokušaja veći ili jednak dozvoljenom broju, blokiraj prijavu
        return attempt.getAttempts() >= MAX_ATTEMPTS;
    }

    // Metod za inkrementiranje broja pokušaja prijave za IP adresu
    public void incrementAttempts(String ip) {
        LoginAttempt attempt = attemptsCache.getOrDefault(ip, new LoginAttempt(ip));
        attempt.incrementAttempts();
        attemptsCache.put(ip, attempt);
    }

    // Metod za resetovanje broja pokušaja (poziva se nakon uspešne prijave)
    public void resetAttempts(String ip) {
        attemptsCache.remove(ip);  // Uklonimo zapis o IP adresi, čime se resetuju pokušaji
    }

    // Interna klasa koja predstavlja podatke o pokušajima prijave za određenu IP adresu
    private static class LoginAttempt {
        private String ip;
        private int attempts;
        private long lastAttempt;

        public LoginAttempt(String ip) {
            this.ip = ip;
            this.attempts = 0;
            this.lastAttempt = System.currentTimeMillis();
        }

        // Metod za inkrementiranje broja pokušaja
        public void incrementAttempts() {
            attempts++;
            lastAttempt = System.currentTimeMillis();  // Ažuriramo vreme poslednjeg pokušaja
        }

        public int getAttempts() {
            return attempts;
        }

        public long getLastAttempt() {
            return lastAttempt;
        }
    }
}

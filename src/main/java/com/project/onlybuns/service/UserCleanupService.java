package com.project.onlybuns.service;

import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserCleanupService {

    @Autowired
    private UserRepository userRepository;

    // Metoda koja se pokreće poslednjeg dana u mesecu u ponoć
    @Scheduled(cron = "0 0 0 L * ?") // Poslednji dan u mesecu u ponoć
    public void deleteInactiveUsers() {
        // Pronađite sve neaktivne korisnike
        List<User> inactiveUsers = userRepository.findByIsActiveFalse();

        // Briši sve neaktivne korisnike
        if (!inactiveUsers.isEmpty()) {
            userRepository.deleteAll(inactiveUsers);
            System.out.println("Deleted " + inactiveUsers.size() + " inactive users.");
        } else {
            System.out.println("No inactive users to delete.");
        }
    }
}


//TESTIRANJE
 /*       package com.project.onlybuns.service;

        import com.project.onlybuns.model.User;
        import com.project.onlybuns.repository.UserRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.scheduling.annotation.Scheduled;
        import org.springframework.stereotype.Service;

        import java.time.LocalDate;
        import java.time.LocalDateTime;
        import java.util.List;

@Service
public class UserCleanupService {

    @Autowired
    private UserRepository userRepository;

    Metoda koja se pokreće svakih 15 minuta*/
    //*@Scheduled(cron = "0 */15 * * * *")
    /*public void deleteInactiveUsersForToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.of(2024, 11, 23); // Datum za koji brišemo korisnike (23. novembar 2024.)

        // Pronađite sve neaktivne korisnike
        List<User> inactiveUsers = userRepository.findByIsActiveFalse();

        // Filtrirajte korisnike registrovane danas, koji su registrovani u poslednjih 15 minuta
        inactiveUsers.removeIf(user -> {
            LocalDateTime registrationDate = user.getRegistrationDate();
            return registrationDate.toLocalDate().equals(today) && registrationDate.isAfter(now.minusMinutes(2));
        });

        // Brišite preostale korisnike
        if (!inactiveUsers.isEmpty()) {
            userRepository.deleteAll(inactiveUsers);
            System.out.println("Deleted " + inactiveUsers.size() + " inactive users for today (23.11.2024).");
        } else {
            System.out.println("No inactive users to delete for today.");
        }
    }
}*/


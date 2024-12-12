package com.project.onlybuns.service;

import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Service
public class UserCleanupService {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 L * ?") // Poslednji dan u mesecu u ponoć
    public void deleteInactiveUsers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesBeforeMidnight = now.with(LocalTime.of(23, 50)); // 23:50 poslednji dan meseca

        // Pronađite sve neaktivne korisnike
        List<User> inactiveUsers = userRepository.findByIsActiveFalse();

        // Filtrirajte korisnike koji su registrovani u poslednjih 10 minuta pre ponoći
        List<User> usersToDelete = inactiveUsers.stream()
                .filter(user -> user.getRegistrationDate().isBefore(tenMinutesBeforeMidnight))
                .toList();

        // Briši samo korisnike koji ne potpadaju pod period zaštite
        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
            System.out.println("Deleted " + usersToDelete.size() + " inactive users.");
        } else {
            System.out.println("No inactive users to delete or protected by time rule.");
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


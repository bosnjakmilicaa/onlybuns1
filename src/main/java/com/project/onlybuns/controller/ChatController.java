package com.project.onlybuns.controller;

import com.project.onlybuns.DTO.ChatGroupDTO;
import com.project.onlybuns.DTO.MessageDTO;
import com.project.onlybuns.model.Message;
import com.project.onlybuns.model.ChatGroup;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.ChatGroupService;
import com.project.onlybuns.repository.MessageRepository;
import com.project.onlybuns.repository.ChatGroupRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @Autowired
    private ChatGroupService chatGroupService;

    // Endpoint za kreiranje nove grupe
    @PostMapping("/create")
    public ChatGroup createGroup(@RequestBody ChatGroup chatGroup) {
        return chatGroupRepository.save(chatGroup);
    }

    // Endpoint za dodavanje člana u grupu
    @PostMapping("/add-member/{groupName}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> addMemberToGroup(
            @PathVariable String groupName,
            @RequestBody List<String> usernames,
            Authentication authentication) {

        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Pronalaženje grupe po imenu
        ChatGroup group = chatGroupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Proveriti da li je ulogovani korisnik admin
        if (!group.getAdmin().getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not an admin of this group");
        }

        // Lista za uspešno dodane korisnike
        List<String> addedUsers = new ArrayList<>();
        // Lista za korisnike koji nisu mogli da budu dodani
        List<String> notFoundUsers = new ArrayList<>();

        for (String username : usernames) {
            // Pronalaženje korisnika po korisničkom imenu
            RegisteredUser userToAdd = registeredUserRepository.findByUsername(username)
                    .orElse(null);

            if (userToAdd != null) {
                // Dodaj korisnika u grupu ako već nije član
                if (!group.getMembers().contains(userToAdd)) {
                    group.getMembers().add(userToAdd);
                    addedUsers.add(username);
                }
            } else {
                notFoundUsers.add(username);
            }
        }

        // Spasiti izmenjenu grupu
        chatGroupRepository.save(group);

        // Kreirati odgovor na osnovu rezultata
        if (!addedUsers.isEmpty() && !notFoundUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .body("Successfully added: " + String.join(", ", addedUsers) +
                            ". The following users were not found: " + String.join(", ", notFoundUsers));
        } else if (!addedUsers.isEmpty()) {
            return ResponseEntity.ok("Successfully added: " + String.join(", ", addedUsers));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("None of the users were found.");
        }
    }


    @DeleteMapping("group/{groupName}/{username}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> removeUserFromGroup(
            @PathVariable String groupName,
            @PathVariable String username,
            Authentication authentication) {

        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Pronalaženje grupe po imenu
        ChatGroup group = chatGroupRepository.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Proveriti da li je ulogovani korisnik admin
        if (!group.getAdmin().getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not an admin of this group");
        }

        // Onemogućiti korisniku da ukloni sebe
        if (loggedInUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot remove yourself from the group");
        }

        // Uklanjanje korisnika iz grupe
        RegisteredUser userToRemove = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean removed = group.getMembers().remove(userToRemove);

        if (removed) {
            chatGroupRepository.save(group); // Spasite izmenjenu grupu
            return ResponseEntity.ok("User removed from group successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in the group");
        }
    }

    // Metoda za slanje poruka
    @MessageMapping("/send")
    public void sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/group/" + message.getChatGroup().getId(), message);
    }



    @GetMapping("/groups")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<ChatGroupDTO> getUserGroups(Authentication authentication) {
        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Dobijanje lista grupa u koje korisnik pripada
        List<ChatGroup> userGroups = chatGroupRepository.findByMembersId(loggedInUser.getId());

        // Ako korisnik nije član nijedne grupe, vrati praznu listu
        if (userGroups.isEmpty()) {
            throw new IllegalArgumentException("No groups found for the user");
        }

        // Mapiranje grupe u DTO
        List<ChatGroupDTO> chatGroupDTOs = new ArrayList<>();
        for (ChatGroup group : userGroups) {
            // Prikupljanje učesnika u grupi
            List<String> participants = group.getMembers().stream()
                    .filter(member -> !member.getUsername().equals(group.getAdmin().getUsername())) // Izostavi admina
                    .map(RegisteredUser::getUsername)
                    .collect(Collectors.toList());

            // Prikupljanje poruka i vremena kada su poslate
            List<MessageDTO> messages = group.getMessages().stream()
                    .map(message -> new MessageDTO(message.getContent(), message.getTimestamp().toString())) // Pretvori datum u string
                    .collect(Collectors.toList());

            // Kreiranje DTO objekta za grupu
            ChatGroupDTO groupDTO = new ChatGroupDTO(
                    group.getName(),
                    group.getAdmin().getUsername(), // Admin je korisnik grupe
                    participants,
                    messages
            );
            chatGroupDTOs.add(groupDTO);
        }

        return chatGroupDTOs;
    }

    // Metoda koja proverava da li je trenutni ulogovani korisnik administrator grupe
    @PreAuthorize("hasRole('REGISTERED')")
    @GetMapping("/isAdminByGroupName/{groupName}")
    public ResponseEntity<Map<String, Object>> isUserAdminByGroupName(@PathVariable String groupName, Authentication authentication) {
        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Pronalaženje grupe na osnovu naziva
        Optional<ChatGroup> chatGroup = chatGroupRepository.findByName(groupName);

        if (chatGroup.isPresent()) {
            ChatGroup group = chatGroup.get();

            // Provera da li je ulogovani korisnik administrator grupe
            boolean isAdmin = group.getAdmin().getId().equals(loggedInUser.getId());

            // Kreiranje odgovora sa informacijom da li je admin
            Map<String, Object> response = new HashMap<>();
            response.put("groupName", group.getName());
            response.put("isAdmin", isAdmin);

            return ResponseEntity.ok(response);
        }

        // Ako grupa nije pronađena, vraćamo 404 grešku
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Group not found"));
    }


    // Endpoint za poslednje poruke u grupi
    @GetMapping("/lastMessages/{groupId}")
    public List<Message> getLastMessages(@PathVariable Long groupId) {
        return messageRepository.findTop10ByChatGroupIdOrderByTimestampDesc(groupId);
    }


    @PostMapping("/createGroup")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<?> createGroup(
            @RequestBody Map<String, String> requestBody, // Prima telo kao JSON
            Authentication authentication) {

        // Ekstrakcija parametra iz requestBody mape
        String groupName = requestBody.get("groupName");

        if (groupName == null || groupName.isBlank()) {
            return ResponseEntity.badRequest().body("Group name is required");
        }

        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Kreiranje grupe sa ulogovanim korisnikom kao adminom
        ChatGroup newGroup = chatGroupService.createGroup(groupName, loggedInUser);

        // Odgovor sa informacijama o grupi
        Map<String, Object> response = new HashMap<>();
        response.put("groupName", newGroup.getName());
        response.put("admin", newGroup.getAdmin().getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




}




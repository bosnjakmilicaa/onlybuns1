package com.project.onlybuns.controller;

import com.project.onlybuns.DTO.PrivateChatResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @MessageMapping("/send")
    public void sendMessage(@Payload MessageDTO messageDTO) {
        // Set timestamp for the message
        String timestampString = LocalDateTime.now().toString();
        messageDTO.setTimestamp(timestampString);

        // Kreirajte instancu ChatGroup koristeći groupName
        logger.info("Looking for chat group with name: " + messageDTO.getGroupName());
        ChatGroup chatGroup = chatGroupRepository.findByName(messageDTO.getGroupName())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Pretražite korisnika na osnovu username-a (senderUsername)
        if (messageDTO.getUsername() == null || messageDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required to send a message");
        }

        RegisteredUser user = registeredUserRepository.findByUsername(messageDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Kreirajte novu poruku
        Message message = new Message();
        message.setContent(messageDTO.getContent());

        // Konvertujte timestamp u LocalDateTime
        LocalDateTime timestamp = LocalDateTime.parse(messageDTO.getTimestamp(), DateTimeFormatter.ISO_DATE_TIME);
        message.setTimestamp(timestamp);

        // Postavite chatGroup i sender (korisnika)
        message.setChatGroup(chatGroup);
        message.setSender(user);

        // Spasite poruku u bazi
        messageRepository.save(message);

        // Dodajte korisničko ime u MessageDTO pre slanja
        messageDTO.setUsername(user.getUsername());

        // Pošaljite poruku svim članovima grupe
        logger.info("Sending message to group: " + messageDTO.getGroupName());
        messagingTemplate.convertAndSend("/topic/group/" + messageDTO.getGroupName(), messageDTO);
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
            return new ArrayList<>();
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

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);


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
    @PostMapping("/createPrivateChat")
    public ResponseEntity<?> createPrivateChat(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String recipientUsername = requestBody.get("participant");

        // Provera da li je uneto korisničko ime primaoca
        if (recipientUsername == null || recipientUsername.isBlank()) {
            return ResponseEntity.badRequest().body("Recipient username is required");
        }

        String senderUsername = authentication.getName();

        // Pronalaženje korisnika na osnovu username
        RegisteredUser sender = registeredUserRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        RegisteredUser recipient = registeredUserRepository.findByUsername(recipientUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient not found"));

        // Kreiranje ili pronalaženje privatnog chata između dva korisnika
        ChatGroup chatGroup = chatGroupService.createOrFindPrivateChat(sender, recipient);

        // Setovanje naziva grupe na osnovu korisničkog imena
        chatGroup.setName(recipientUsername);

        chatGroupRepository.save(chatGroup);

        // Kreiranje DTO odgovora sa željenim podacima
        PrivateChatResponseDTO chatResponseDTO = new PrivateChatResponseDTO();
        chatResponseDTO.setSenderUsername(sender.getUsername());
        chatResponseDTO.setRecipientUsername(recipient.getUsername());
        chatResponseDTO.setGroupName(chatGroup.getName());

        return ResponseEntity.ok(chatResponseDTO); // Vraća samo potrebne podatke
    }



    @PreAuthorize("hasRole('REGISTERED')")
    @DeleteMapping("/deleteGroup/{groupName}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName, Authentication authentication) {
        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Pronalaženje grupe na osnovu naziva
        Optional<ChatGroup> chatGroupOpt = chatGroupRepository.findByName(groupName);

        if (chatGroupOpt.isPresent()) {
            ChatGroup group = chatGroupOpt.get();

            // Provera da li je ulogovani korisnik administrator grupe
            if (!group.getAdmin().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only the group admin can delete the group");
            }

            // Ako je korisnik admin, brišemo grupu
            chatGroupRepository.delete(group);

            return ResponseEntity.ok("Group deleted successfully");
        }

        // Ako grupa nije pronađena, vraćamo 404 grešku
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Group not found");
    }

    /*@GetMapping("/groups/{groupName}/messages")
    public List<MessageDTO> getMessagesByGroupName(@PathVariable String groupName) {
        // Dohvati poruke koristeći ime grupe
        List<Message> messages = messageRepository.findMessagesWithSenderByGroupName(groupName);

        // Mapiraj poruke u DTO
        return messages.stream().map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setContent(message.getContent());
            dto.setTimestamp(message.getTimestamp().toString());

            // Dodajte logovanje kako biste proverili da li je sender pravilno učitan
            if (message.getSender() != null) {
                System.out.println("Sender ID: " + message.getSender().getId()); // Proverite sender ID
                dto.setSenderId(message.getSender().getId());
            } else {
                System.out.println("Sender is null"); // Ako je sender null, logujte to
                dto.setSenderId(null);
            }

            return dto;
        }).collect(Collectors.toList());
    }*/

    @GetMapping("/groups/{groupName}/messages")
    public List<MessageDTO> getMessagesByGroupName(@PathVariable String groupName) {
        // Dohvati poruke koristeći ime grupe
        List<Message> messages = messageRepository.findMessagesWithSenderByGroupName(groupName);

        // Mapiraj poruke u DTO
        return messages.stream().map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setContent(message.getContent());
            dto.setTimestamp(message.getTimestamp().toString());

            // Proverite da li je sender validan
            if (message.getSender() != null) {
                // Logovanje Sender ID-a
                System.out.println("Sender ID: " + message.getSender().getId());

                // Pronalaženje korisnika na osnovu sender ID
                RegisteredUser sender = message.getSender();
                dto.setSenderId(sender.getId()); // Postavite senderId u DTO

                // Dodeljivanje username-a u DTO
                dto.setUsername(sender.getUsername()); // Dodajte username
            } else {
                System.out.println("Sender is null");
                dto.setSenderId(null); // Postavite null ako je sender null
                dto.setUsername(null); // Postavite null ako sender nije prisutan
            }

            return dto;
        }).collect(Collectors.toList());
    }






}




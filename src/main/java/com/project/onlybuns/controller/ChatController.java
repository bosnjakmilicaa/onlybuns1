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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @PostMapping("/add-member/{groupId}/{userId}")
    public String addMemberToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        chatGroupService.addMemberToGroup(groupId, userId);
        return "User added to the group successfully";
    }

    // Endpoint za uklanjanje člana iz grupe
    @PostMapping("/remove-member/{groupId}/{userId}")
    public String removeMemberFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        Optional<ChatGroup> chatGroup = chatGroupRepository.findById(groupId);
        Optional<RegisteredUser> user = registeredUserRepository.findById(userId);
        if (chatGroup.isPresent() && user.isPresent()) {
            chatGroup.get().removeMember(user.get());
            chatGroupRepository.save(chatGroup.get());
            return "User removed from the group successfully";
        }
        return "Group or user not found";
    }

    // Metoda za slanje poruka
    @MessageMapping("/send")
    public void sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/group/" + message.getChatGroup().getId(), message);
    }



    /*@GetMapping("/groups")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<ChatGroup> getUserGroups(Authentication authentication) {
        // Izvlačenje korisničkog imena iz autentifikacije
        String loggedInUsername = authentication.getName();

        // Pronalaženje registrovanog korisnika na osnovu korisničkog imena
        RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));

        // Dobijanje lista grupa u koje korisnik pripada
        List<ChatGroup> userGroups = chatGroupRepository.findByMembersId(loggedInUser.getId());

        if (userGroups.isEmpty()) {
            throw new IllegalArgumentException("No groups found for the user");
        }

        return userGroups;
    }*/

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




    // Endpoint za poslednje poruke u grupi
    @GetMapping("/lastMessages/{groupId}")
    public List<Message> getLastMessages(@PathVariable Long groupId) {
        return messageRepository.findTop10ByChatGroupIdOrderByTimestampDesc(groupId);
    }
}

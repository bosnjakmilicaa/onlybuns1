package com.project.onlybuns.service;

import com.project.onlybuns.model.ChatGroup;
import com.project.onlybuns.model.Message;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.ChatGroupRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;
    private final MessageService messageService; // Dodata zavisnost

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    public ChatGroupService(ChatGroupRepository chatGroupRepository, MessageService messageService) {
        this.chatGroupRepository = chatGroupRepository;
        this.messageService = messageService; // Inicijalizacija
    }

    public ChatGroup createGroup(String groupName, RegisteredUser admin) {
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setName(groupName);
        chatGroup.setAdmin(admin);
        chatGroup.getMembers().add(admin); // Admin je uvek član grupe
        return chatGroupRepository.save(chatGroup);
    }
    public void addMemberToGroup(Long groupId, Long userId) {
        // Pronađi grupu
        ChatGroup chatGroup = chatGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        // Pronađi korisnika
        RegisteredUser user = registeredUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Dodaj korisnika u grupu
        chatGroup.addMember(user);

        // Spasite promene
        chatGroupRepository.save(chatGroup);
    }

    public void addMemberToGroup(Long groupId, RegisteredUser user) {
        Optional<ChatGroup> chatGroupOpt = chatGroupRepository.findById(groupId);
        if (chatGroupOpt.isPresent()) {
            ChatGroup chatGroup = chatGroupOpt.get();
            chatGroup.getMembers().add(user);
            chatGroupRepository.save(chatGroup);
        }
    }

    public void removeMemberFromGroup(Long groupId, RegisteredUser user) {
        Optional<ChatGroup> chatGroupOpt = chatGroupRepository.findById(groupId);
        if (chatGroupOpt.isPresent()) {
            ChatGroup chatGroup = chatGroupOpt.get();
            chatGroup.getMembers().remove(user);
            chatGroupRepository.save(chatGroup);
        }
    }

    // Funkcija za dohvat prethodnih 10 poruka
    public List<Message> getRecentMessages(Long groupId) {
        return messageService.getMessagesForGroup(groupId) // Poziv metode iz MessageService
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteGroupByName(String groupName) {
        // Pronađite grupu prema imenu
        ChatGroup chatGroup = chatGroupRepository.findByName(groupName)
                .orElseThrow(() -> new RuntimeException("Group not found"));


        // Obrišite grupu
        chatGroupRepository.delete(chatGroup);
    }

}

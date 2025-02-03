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
    public ChatGroup createOrFindPrivateChat(RegisteredUser sender, RegisteredUser recipient) {
        String privateChatName = sender.getUsername() + "-" + recipient.getUsername();

        // Proveri da li već postoji čet između ova dva korisnika
        Optional<ChatGroup> existingChat = chatGroupRepository.findByName(privateChatName);

        if (existingChat.isPresent()) {
            return existingChat.get();
        }

        // Kreiraj novi privatni čet
        ChatGroup privateChat = new ChatGroup();
        privateChat.setName(privateChatName);
        privateChat.setAdmin(sender);
        privateChat.getMembers().add(sender);
        privateChat.getMembers().add(recipient);

        return chatGroupRepository.save(privateChat);
    }

}

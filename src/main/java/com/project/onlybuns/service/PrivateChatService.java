package com.project.onlybuns.service;

import com.project.onlybuns.model.ChatGroup;
import com.project.onlybuns.model.Message;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.ChatGroupRepository;
import com.project.onlybuns.repository.MessageRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PrivateChatService {

    private final ChatGroupRepository chatGroupRepository;
    private final MessageRepository messageRepository;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    public PrivateChatService(ChatGroupRepository chatGroupRepository, MessageRepository messageRepository) {
        this.chatGroupRepository = chatGroupRepository;
        this.messageRepository = messageRepository;
    }

    /*public ChatGroup sendPrivateMessage(String senderUsername, String receiverUsername, String content) {
        // Pronađi korisnike preko username-a
        RegisteredUser sender = registeredUserRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        RegisteredUser receiver = registeredUserRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Proveri da li već postoji privatni chat između korisnika
        //Optional<ChatGroup> existingChat = chatGroupRepository.findPrivateChatBetweenUsers(sender.getUsername(), receiver.getUsername());
        ChatGroup privateChat;

        /*if (existingChat.isPresent()) {
            privateChat = existingChat.get();
        } else {
            // Kreiraj novi privatni chat
            privateChat = new ChatGroup();
            privateChat.setName("Private Chat: " + senderUsername + " & " + receiverUsername);
            privateChat.setPrivate(true); // Obeleži kao privatni chat
            privateChat.getMembers().add(sender);
            privateChat.getMembers().add(receiver);

            chatGroupRepository.save(privateChat);
        }

        // Kreiraj novu poruku
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setChatGroup(privateChat);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);*/

        /*return privateChat;
    }*/
}

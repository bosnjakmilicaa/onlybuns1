package com.project.onlybuns.controller;

import com.project.onlybuns.model.Message;
import com.project.onlybuns.model.ChatGroup;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.MessageService;
import com.project.onlybuns.service.ChatGroupService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final ChatGroupService chatGroupService;

    public ChatController(MessageService messageService, ChatGroupService chatGroupService) {
        this.messageService = messageService;
        this.chatGroupService = chatGroupService;
    }

    @MessageMapping("/send/message")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        // Snimanje poruke u bazu podataka
        messageService.saveMessage(message);
        return message;
    }

    // Metoda za slanje poruke specifičnim korisnicima
    @MessageMapping("/send/private")
    @SendTo("/topic/private")
    public Message sendPrivateMessage(Message message) {
        messageService.saveMessage(message);
        return message;
    }
}

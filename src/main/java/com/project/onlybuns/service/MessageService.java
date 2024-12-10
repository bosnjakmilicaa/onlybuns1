package com.project.onlybuns.service;

import com.project.onlybuns.model.Message;
import com.project.onlybuns.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessagesForGroup(Long groupId) {
        return messageRepository.findByChatGroupId(groupId);
    }
}

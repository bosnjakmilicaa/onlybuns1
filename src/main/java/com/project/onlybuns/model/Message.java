package com.project.onlybuns.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RegisteredUser sender; // Pošiljalac poruke

    @ManyToOne
    private ChatGroup chatGroup; // Grupa kojoj pripada poruka

    @Column(nullable = false)
    private String content; // Sadržaj poruke

    @Column(nullable = false)
    private LocalDateTime timestamp; // Vreme slanja poruke

    // Getteri i Setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegisteredUser getSender() {
        return sender;
    }

    public void setSender(RegisteredUser sender) {
        this.sender = sender;
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

package com.project.onlybuns.DTO;

public class MessageDTO {

    private String content; // Sadržaj poruke
    private String timestamp; // Vreme kada je poruka poslata
    private Long chatGroupId; // ID chat grupe
    private String username; // Korisničko ime pošiljaoca

    private Long senderId; // ID pošiljaoca

    // Konstruktor
    public MessageDTO(String content, String timestamp, Long senderId) {
        this.content = content;
        this.timestamp = timestamp;
        this.senderId = senderId; // Dodeljujemo ID pošiljaoca prilikom kreiranja poruke
    }


    // Konstruktor
    public MessageDTO(String content, String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getteri i setteri
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(Long chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}

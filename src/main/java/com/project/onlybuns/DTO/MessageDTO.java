package com.project.onlybuns.DTO;

public class MessageDTO {

    private String content; // Sadržaj poruke
    private String timestamp; // Vreme kada je poruka poslata

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
}

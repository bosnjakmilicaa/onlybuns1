package com.project.onlybuns.DTO;

import java.util.List;

public class ChatGroupDTO {

    private String groupName; // Naziv grupe
    private String adminName; // Ime admina
    private List<String> participants; // Lista korisničkih imena članova
    private List<MessageDTO> messages; // Lista poruka u grupi

    // Konstruktor
    public ChatGroupDTO(String groupName, String adminName, List<String> participants, List<MessageDTO> messages) {
        this.groupName = groupName;
        this.adminName = adminName;
        this.participants = participants;
        this.messages = messages;
    }

    // Getteri i setteri
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}

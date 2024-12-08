package com.project.onlybuns.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Naziv grupe

    @ManyToOne
    private RegisteredUser admin; // Korisnik koji je admin grupe

    @ManyToMany
    @JoinTable(
            name = "chat_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<RegisteredUser> members = new HashSet<>(); // Članovi grupe

    @OneToMany(mappedBy = "chatGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>(); // Poruke u grupi

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegisteredUser getAdmin() {
        return admin;
    }

    public void setAdmin(RegisteredUser admin) {
        this.admin = admin;
    }

    public Set<RegisteredUser> getMembers() {
        return members;
    }

    public void setMembers(Set<RegisteredUser> members) {
        this.members = members;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    // Dodavanje člana u grupu
    public void addMember(RegisteredUser user) {
        members.add(user);
    }

    // Uklanjanje člana iz grupe
    public void removeMember(RegisteredUser user) {
        members.remove(user);
    }
}

package com.project.onlybuns.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Naziv grupe

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false) // Korisnik koji je admin grupe
    private RegisteredUser admin; // Admin korisnik

    @ManyToMany
    @JoinTable(
            name = "chat_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<RegisteredUser> members = new HashSet<>(); // Članovi


    @OneToMany(mappedBy = "chatGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>(); // Poruke u grupi
    @Column(name = "flag")
    private Integer flag =0; // Dodaj flag

    public Integer getFlag() {
        return Optional.ofNullable(flag).orElse(0); // Ako je flag null, vraća 0
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

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

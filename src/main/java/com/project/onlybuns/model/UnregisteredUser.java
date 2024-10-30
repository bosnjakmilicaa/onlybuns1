package com.project.onlybuns.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("unregistered_user")
public class UnregisteredUser extends User {

    private String email;
    private String firstName;
    private String lastName;
    private String address; // User's address
    private boolean isActive; // Status of the user's account

    // Default constructor
    public UnregisteredUser() {
        super();
        this.isActive = false; // By default, user is not active
    }

    // Constructor with parameters
    public UnregisteredUser(String username, String password, String email, String firstName, String lastName, String address) {
        super(username, password);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.isActive = false; // New user starts inactive
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Method to validate user input during registration
    /*public boolean validateRegistration() {
        // Add validation logic for email, username, and password
        // For example, check if fields are not empty, if email is valid, etc.
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return false; // Invalid email
        }
        if (getUsername() == null || getUsername().isEmpty()) {
            return false; // Invalid username
        }
        if (getPassword() == null || getPassword().length() < 6) {
            return false; // Password must be at least 6 characters
        }
        // More validation can be added as needed
        return true;
    }*/



    public boolean validateRegistration() {
        // Provera da li je email prazan i da li ima validan format
        if (email == null || email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false; // Nevalidan email
        }

        // Provera da li je korisničko ime prazno
        if (getUsername() == null || getUsername().isEmpty()) {
            return false; // Korisničko ime ne sme biti prazno
        }

        // Provera da li je korisničko ime dovoljno dugačko
        if (getUsername().length() < 3) {
            return false; // Korisničko ime mora imati bar 3 karaktera
        }

        // Provera da li je lozinka prazna ili kraća od 6 karaktera
        if (getPassword() == null || getPassword().length() < 6) {
            return false; // Lozinka mora imati bar 6 karaktera
        }

        // Provera da li je ime prazno
        if (firstName == null || firstName.isEmpty()) {
            return false; // Ime ne sme biti prazno
        }

        // Provera da li je prezime prazno
        if (lastName == null || lastName.isEmpty()) {
            return false; // Prezime ne sme biti prazno
        }

        // Provera da li je adresa prazna
        if (address == null || address.isEmpty()) {
            return false; // Adresa ne sme biti prazna
        }

        // Ako sve validacije prođu, vraća true
        return true;
    }

}

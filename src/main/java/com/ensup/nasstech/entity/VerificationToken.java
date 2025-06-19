package com.ensup.nasstech.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;

    private String token;
    private LocalDateTime expirationDate;

    @OneToOne
    private Utilisateur utilisateur;

    public VerificationToken() {}

    public VerificationToken(String token, Utilisateur utilisateur, String type) {
        this.token = token;
        this.utilisateur = utilisateur;
        this.type = type;
        
        if ("RESET".equals(type)) {
            this.expirationDate = LocalDateTime.now().plusHours(1);
        } else if ("VERIFICATION".equals(type)) {
            this.expirationDate = LocalDateTime.now().plusHours(24);
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }
    private String type; // "VERIFICATION" ou "RESET"
    
    // Getters et setters
    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
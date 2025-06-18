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

    public VerificationToken(String token, Utilisateur utilisateur) {
        this.token = token;
        this.utilisateur = utilisateur;
        this.expirationDate = LocalDateTime.now().plusHours(24); // Jeton valide 24 heures
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

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
}
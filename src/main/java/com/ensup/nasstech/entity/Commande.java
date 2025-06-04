package com.ensup.nasstech.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Commande {
    @Id
    @GeneratedValue
    private Long id;
    private Date dateAchat;
    @ManyToOne
    private Ordinateur ordinateur;

    public Commande() {}

    public Commande(Ordinateur ordinateur, Date dateAchat) {
        this.ordinateur = ordinateur;
        this.dateAchat = dateAchat;
    }
    @Override
    public String toString() {
        return "Achat [id=" + id + ", dateAchat=" + dateAchat + ", ordinateur=" + ordinateur + "]";
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }

    public Ordinateur getOrdinateur() {
        return ordinateur;
    }

    public void setOrdinateur(Ordinateur ordinateur) {
        this.ordinateur = ordinateur;
    }
    
}

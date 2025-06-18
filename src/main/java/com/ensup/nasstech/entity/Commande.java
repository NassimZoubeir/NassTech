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
    private Date dateCommande;
    @ManyToOne
    private Ordinateur ordinateur;

    public Commande() {}

    public Commande(Ordinateur ordinateur, Date dateCommande) {
        this.ordinateur = ordinateur;
        this.dateCommande = dateCommande;
    }
    @Override
    public String toString() {
        return "Commande [id=" + id + ", dateCommande=" + dateCommande + ", ordinateur=" + ordinateur + "]";
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Ordinateur getOrdinateur() {
        return ordinateur;
    }

    public void setOrdinateur(Ordinateur ordinateur) {
        this.ordinateur = ordinateur;
    }
    
}

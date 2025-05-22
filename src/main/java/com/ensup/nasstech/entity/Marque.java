package com.ensup.nasstech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Marque {

	@Id
    @GeneratedValue
    private Long id;
    private String nom;

    public Marque() {}

    public Marque(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Marque [id=" + id + ", nom=" + nom + "]";
    }
}

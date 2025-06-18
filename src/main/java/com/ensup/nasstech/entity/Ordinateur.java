package com.ensup.nasstech.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ordinateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String denomination;
    private double prix;
    private String processeur;
    private double ecran;
    private int vive;
    private String image;
    private int nombreOrdinateur;
    
    @ManyToOne
    private Marque marque;

	public Ordinateur() {}

    public Ordinateur(String denomination, double prix, String processeur, double ecran, int vive, String image,int nombreOrdinateur,Marque marque) {
        this.denomination = denomination;
        this.prix = prix;
        this.processeur = processeur;
        this.ecran = ecran;
        this.vive = vive;
        this.image = image;
        this.nombreOrdinateur = nombreOrdinateur;
        this.marque = marque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getProcesseur() {
        return processeur;
    }

    public void setProcesseur(String processeur) {
        this.processeur = processeur;
    }

    public double getEcran() {
        return ecran;
    }

    public void setEcran(double ecran) {
        this.ecran = ecran;
    }

    public int getVive() {
        return vive;
    }

    public void setVive(int vive) {
        this.vive = vive;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

	public int getNombreOrdinateur() {
		return nombreOrdinateur;
	}

	public void setNombreOrdinateur(int nombreOrdinateur) {
		this.nombreOrdinateur = nombreOrdinateur;
	}

	public Marque getMarque() {
		return marque;
	}

	public void setMarque(Marque marque) {
		this.marque = marque;
	}
	@Override
	public String toString() {
		return "Ordinateur [id=" + id + ", denomination=" + denomination + ", prix=" + prix + ", processeur="
				+ processeur + ", ecran=" + ecran + ", vive=" + vive + ", image=" + image + ", nombreOrdinateur=" + nombreOrdinateur + ", marque=" + marque + "]";
	}
}

package com.ensup.nasstech.entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Représente un utilisateur de la plateforme NassTech.
 * Un utilisateur peut passer des commandes d'ordinateurs.
 */
@Entity
public class Utilisateur {
	
	  /** Identifiant unique de l'utilisateur. */
    @Id
    @GeneratedValue
    private Long id;

    /** Nom d'utilisateur (login). */
    private String login;

    /** Hash du mot de passe. */
    private String passwdHash;

    /** Adresse email de l'utilisateur. */
    private String email;

    /** Rôle de l'utilisateur (ex: administrateur, utilisateur). */
    private String role;

    /** Adresse postale de l'utilisateur. */
    private String adresse;

    /** Liste des commandes associées à l'utilisateur. */
    @OneToMany
    private List<Commande> commanderOrdinateurList;

    /** Indique si le compte est vérifié par email. */
    private boolean verified = false;

    /** Constructeur par défaut. */
    public Utilisateur() {}

    /**
     * Constructeur principal.
     * @param login Nom d'utilisateur
     * @param passwdHash Mot de passe hashé
     * @param email Adresse email
     * @param role Rôle de l'utilisateur
     * @param adresse Adresse postale
     */
    public Utilisateur(String login, String passwdHash, String email, String role, String adresse) {
        this.login = login;
        this.passwdHash = passwdHash;
        this.email = email;
        this.role = role;
        this.adresse = adresse;
        commanderOrdinateurList = new ArrayList<>();
    }

    /**
     * Ajoute une commande à la liste de l'utilisateur.
     * @param commande Commande à ajouter
     */
    public void ajouterCommande(Commande commande) {
        commanderOrdinateurList.add(commande);
    }
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPasswdHash() {
		return passwdHash;
	}
	public void setPasswdHash(String passwdHash) {
		this.passwdHash = passwdHash;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAdresse() {
	    return adresse;
	}

	public void setAdresse(String adresse) {
	    this.adresse = adresse;
	}
	public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
	public List<Commande> getCommanderOrdinateurList() {
		return commanderOrdinateurList;
	}
	public void setCommanderOrdinateurList(List<Commande> commanderOrdinateurList) {
		this.commanderOrdinateurList = commanderOrdinateurList;
	}
	public void commanderOrdinateur(Commande commande) {
		
	}
}

package com.ensup.nasstech.entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Utilisateur {
	@Id
	@GeneratedValue
	private Long id;
	private String login;
	private String passwdHash;
	private String email;
	private String role;
	private String adresse;
	@OneToMany
	private List<Commande> commanderOrdinateurList;
	
	private boolean verified = false;
	
	public Utilisateur() {}
	public Utilisateur(String login, String passwdHash, String email, String role, String adresse) {
		this.login = login;
		this.passwdHash = passwdHash;
		this.email = email;
		this.role = role;
		this.adresse = adresse;
		commanderOrdinateurList = new ArrayList<Commande>();
	}
	@Override
	public String toString() {
		return "Utilisateur [login=" + login + ", id=" + id + ", passwdHash=" + passwdHash + ", email=" + email + ", role=" + role +"]";
	}
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

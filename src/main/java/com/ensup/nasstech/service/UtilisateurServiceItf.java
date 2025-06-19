package com.ensup.nasstech.service;

import java.util.List;

import com.ensup.nasstech.entity.Commande;
import com.ensup.nasstech.entity.Utilisateur;

public interface UtilisateurServiceItf {
	void creerUtilisateur(Utilisateur utilisateur);
	Utilisateur lireUtilisateurParLogin(String login);
	Utilisateur lireUtilisateurParId(Long id);
	void passerCommandeOrdinateurs(List<Long> ordinateurIdList, Long idUtilisateur);
	List<Commande> getCommandeOrdinateurList(Long idUtilisateur);
	Commande getCommandeById(Long id);
	void majCommande(Commande commande);
	boolean verifierEmail(String token);
	void envoyerLienReinitialisation(Utilisateur utilisateur);
}
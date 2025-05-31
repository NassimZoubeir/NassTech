package com.ensup.nasstech.service;

import java.util.List;

import com.ensup.nasstech.entity.Achat;
import com.ensup.nasstech.entity.Utilisateur;

public interface UtilisateurServiceItf {
	void creerUtilisateur(Utilisateur utilisateur);
	Utilisateur lireUtilisateurParLogin(String login);
	Utilisateur lireUtilisateurParId(Long id);
	void acheterListOrdinateurUtilisateur(List<Long>  livreIdList,  Long  idUtilisateur);
	List<Achat> getAchatOrdinateurList(Long idUtilisateur);
	Achat getAchatById(Long id);
	void majAchat(Achat achat);
	boolean verifierEmail(String token);
}
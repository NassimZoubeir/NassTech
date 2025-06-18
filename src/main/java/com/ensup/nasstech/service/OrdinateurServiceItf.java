package com.ensup.nasstech.service;

import java.util.List;

import com.ensup.nasstech.entity.Ordinateur;


public interface OrdinateurServiceItf {
	List<Ordinateur> getAllOrdinateur();
	void creerOrdinateur(Ordinateur ordinateur);
	Ordinateur getOrdinateurById(Long id);
	List<Ordinateur> getOrdinateurCommanderListParOrdinateurIdList(List<Long> ordinateurCommanderListId);
	void incrementernombreOrdinateur(Long id);
	void decrementernombreOrdinateur(Long id);
	List<Ordinateur> rechercherOrdinateurs(String keyword);

}

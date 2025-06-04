package com.ensup.nasstech.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.repository.OrdinateurRepository;

@Service
public class OrdinateurService implements OrdinateurServiceItf {
	@Autowired
	private OrdinateurRepository ordinateurRepository;

	@Override
	public List<Ordinateur> getAllOrdinateur() {
		return ordinateurRepository.findAll();
	}
	@Override
	 public void creerOrdinateur(Ordinateur ordinateur) {
		ordinateurRepository.save(ordinateur);
	 }
	@Override
	 public Ordinateur getOrdinateurById(Long id) {
		Ordinateur ordinateur = ordinateurRepository.findById(id).get();
		return ordinateur;
	 }
	@Override
	public List<Ordinateur> getOrdinateurCommanderListParOrdinateurIdList(List<Long> ordinateurCommanderListId) {
		List<Ordinateur> ordinateurCommanderList = new ArrayList<>();
		for(int i=0; i < ordinateurCommanderListId.size(); i++) {
			ordinateurCommanderList.add(getOrdinateurById(ordinateurCommanderListId.get(i)));
		}
		return ordinateurCommanderList;
	}
	@Override
	public void decrementernombreOrdinateur(Long id) {
		ordinateurRepository.decrementernombreOrdinateur(id);
	}
	@Override
	public void incrementernombreOrdinateur(Long id) {
		ordinateurRepository.incrementernombreOrdinateur(id);
	}
	@Override
	public List<Ordinateur> rechercherOrdinateurs(String keyword) {
	    return ordinateurRepository.searchOrdinateurs(keyword);
	}

	
}
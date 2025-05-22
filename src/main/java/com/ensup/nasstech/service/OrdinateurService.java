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
	public List<Ordinateur> getOrdinateurAcheterListParOrdinateurIdList(List<Long> ordinateurAcheterListId) {
		List<Ordinateur> ordinateurAcheterList = new ArrayList<>();
		for(int i=0; i < ordinateurAcheterListId.size(); i++) {
			ordinateurAcheterList.add(getOrdinateurById(ordinateurAcheterListId.get(i)));
		}
		return ordinateurAcheterList;
	}
	@Override
	public void decrementernombreOrdinateur(Long id) {
		ordinateurRepository.decrementernombreOrdinateur(id);
	}
	@Override
	public void incrementernombreOrdinateur(Long id) {
		ordinateurRepository.incrementernombreOrdinateur(id);
	}
	
}

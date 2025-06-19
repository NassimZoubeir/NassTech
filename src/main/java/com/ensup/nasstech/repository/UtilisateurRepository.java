package com.ensup.nasstech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensup.nasstech.entity.Utilisateur;


public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	Utilisateur findByLogin(String login);
	Utilisateur findByEmail(String email);
}
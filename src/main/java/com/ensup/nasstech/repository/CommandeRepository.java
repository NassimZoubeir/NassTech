package com.ensup.nasstech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensup.nasstech.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {}

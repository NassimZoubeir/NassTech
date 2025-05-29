package com.ensup.nasstech.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.entity.VerificationToken;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    VerificationToken findByUtilisateur(Utilisateur utilisateur);
}
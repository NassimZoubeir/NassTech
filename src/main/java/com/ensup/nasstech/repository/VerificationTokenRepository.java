package com.ensup.nasstech.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.entity.VerificationToken;

import jakarta.transaction.Transactional;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    VerificationToken findByUtilisateur(Utilisateur utilisateur);
    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationToken vt WHERE vt.utilisateur.id = :utilisateurId")
    void deleteByUtilisateurId(@Param("utilisateurId") Long utilisateurId);

}
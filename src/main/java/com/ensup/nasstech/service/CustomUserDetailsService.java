package com.ensup.nasstech.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.ensup.nasstech.entity.Utilisateur;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurServiceItf utilisateurService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurService.lireUtilisateurParLogin(login);
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }

        return new org.springframework.security.core.userdetails.User(
                utilisateur.getLogin(),
                utilisateur.getPasswdHash(),
                List.of(new SimpleGrantedAuthority(utilisateur.getRole())) // Exemple : ROLE_administrateur
        );
    }
}

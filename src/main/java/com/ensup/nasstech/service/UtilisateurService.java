package com.ensup.nasstech.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ensup.nasstech.entity.Commande;
import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.entity.VerificationToken;
import com.ensup.nasstech.repository.CommandeRepository;
import com.ensup.nasstech.repository.OrdinateurRepository;
import com.ensup.nasstech.repository.UtilisateurRepository;
import com.ensup.nasstech.repository.VerificationTokenRepository;


/**
 * Service métier pour la gestion des utilisateurs (création, vérification, commandes, etc.).
 */
@Service
public  class UtilisateurService implements UtilisateurServiceItf {
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailServiceImpl emailService;
	
	@Autowired
	private  OrdinateurRepository  ordinateurRepository;

	@Autowired
	private  OrdinateurServiceItf  ordinateurService;
	
	@Autowired
	private CommandeRepository commandeRepository;
	

	 /**
     * Crée un utilisateur dans la base de données et envoie un email de vérification.
     * @param utilisateur L'utilisateur à enregistrer
     */
	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		utilisateurRepository.save(utilisateur);	
		
		// Supprimer l'ancien token si existant
		VerificationToken existingToken = verificationTokenRepository.findByUtilisateur(utilisateur);
		if (existingToken != null) {
		    verificationTokenRepository.delete(existingToken);
		}

		// Générer un nouveau jeton
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken(token, utilisateur,"VERIFICATION");
		verificationTokenRepository.save(verificationToken);


	    // Envoi de l'email
	    String verificationUrl = "http://localhost:8080/verifier-email?token=" + token;
	    String subject = "Confirmez votre email";
	    String body = "Bonjour " + utilisateur.getLogin() + ",\n\nCliquez sur ce lien pour vérifier votre email :\n" + verificationUrl;

	    // Utiliser le service d'email correctement injecté
	    emailService.sendSimpleMessage(utilisateur.getEmail(), subject, body);
	}
	
	 /**
     * Vérifie l'email via un token.
     * @param token Jeton de vérification
     * @return true si vérification réussie
     */
	@Override
	public boolean verifierEmail(String token) {
	    var optionalToken = verificationTokenRepository.findByToken(token);

	    if (optionalToken.isPresent()
	        && !optionalToken.get().isExpired()
	        && "VERIFICATION".equals(optionalToken.get().getType())) {

	        Utilisateur utilisateur = optionalToken.get().getUtilisateur();
	        utilisateur.setVerified(true);
	        utilisateurRepository.save(utilisateur);

	        // Supprimer le token une fois utilisé
	        verificationTokenRepository.delete(optionalToken.get());

	        return true;
	    }

	    return false;
	}

	/**
     * Envoie un lien de réinitialisation de mot de passe.
     * @param utilisateur Utilisateur concerné
     */
	public void envoyerLienReinitialisation(Utilisateur utilisateur) {
	    String token = UUID.randomUUID().toString();
	    VerificationToken resetToken = new VerificationToken(token, utilisateur, "RESET");
	    verificationTokenRepository.save(resetToken);

	    String resetUrl = "http://localhost:8080/reinitialiser-mot-de-passe?token=" + token;
	    String subject = "Réinitialisation de votre mot de passe - NassTech";
	    String body = "Bonjour " + utilisateur.getLogin() + ",\n\nCliquez sur ce lien pour réinitialiser votre mot de passe :\n" + resetUrl;

	    emailService.sendSimpleMessage(utilisateur.getEmail(), subject, body);
	}

	/**
     * Recherche un utilisateur par son identifiant de connexion.
     * @param login Le login (nom d'utilisateur)
     * @return L'utilisateur correspondant ou null s'il n'existe pas
     */
	@Override
	public Utilisateur lireUtilisateurParLogin(String login) {
		return utilisateurRepository.findByLogin(login);
	}
	/**
     * Recherche un utilisateur par son identifiant unique.
     * @param id L'identifiant de l'utilisateur
     * @return L'utilisateur correspondant
     */
	@Override
	public Utilisateur lireUtilisateurParId(Long id) {
		Utilisateur utilisateur = utilisateurRepository.findById(id).get();
		return utilisateur;
	}
	/**
     * Récupère la liste des commandes passées par un utilisateur.
     * @param idUtilisateur L'identifiant de l'utilisateur
     * @return La liste des commandes effectuées
     */
	@Override
	public List<Commande> getCommandeOrdinateurList(Long idUtilisateur) {
		Utilisateur utilisateur = lireUtilisateurParId(idUtilisateur);
		System.out.println("UtilisateurService - getCommandeOrdinateurList utilisateur:" + utilisateur);
		return utilisateur.getCommanderOrdinateurList();
	}
	 /**
     * Recherche une commande spécifique par son ID.
     * @param id L'identifiant de la commande
     * @return La commande trouvée
     */
	@Override
	public Commande getCommandeById(Long id) {
	    return commandeRepository.findById(id).get();
	}

	 /**
     * Passe commande pour une liste d'ordinateurs pour un utilisateur donné.
     */
	@Override
	public void passerCommandeOrdinateurs(List<Long> ordinateurIdList, Long idUtilisateur) {
	    Utilisateur utilisateur = lireUtilisateurParId(idUtilisateur);
	    List<Ordinateur> ordinateurList = ordinateurService.getOrdinateurCommanderListParOrdinateurIdList(ordinateurIdList);
	    Commande commande = null;
	    for (Ordinateur ordinateur : ordinateurList) {
	        commande = new Commande(ordinateur, new Date());
	        commandeRepository.save(commande);
	        utilisateur.ajouterCommande(commande);
	    }
	    utilisateurRepository.save(utilisateur);
	}

	 /**
     * Met à jour une commande.
     */
	@Override
	public void majCommande(Commande commande) {
	    commandeRepository.save(commande);
	}

	
}
package com.ensup.nasstech.controller;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.entity.VerificationToken;
import com.ensup.nasstech.outil.Outil;
import com.ensup.nasstech.repository.UtilisateurRepository;
import com.ensup.nasstech.repository.VerificationTokenRepository;
import com.ensup.nasstech.service.UtilisateurServiceItf;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public  class  UtilisateurController  {
	
	@Autowired
	private UtilisateurServiceItf utilisateurService;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private VerificationTokenRepository verificationtokenRepository;
	
	@Autowired
	 private PasswordEncoder passwordEncoder;

	@GetMapping("/creer-compte")
	public  String  creerUtilisateur()  {
		return  "creer-utilisateur";
	}
	@GetMapping("/verifier-email")
	public String verifierEmail(@RequestParam String token, Model model) {
	    System.out.println("Token reçu : " + token);

	    boolean isVerified = utilisateurService.verifierEmail(token);

	    if (isVerified) {
	        model.addAttribute("message", "Votre email a été vérifié avec succès !");
	    } else {
	        model.addAttribute("message", "Le jeton est invalide ou expiré.");
	    }

	    return "verification";
	}
	
	@RequestMapping("/creer-compte-validation")
	public String creerUtilisateurValidation(
	        @RequestParam String login,
	        @RequestParam String password,
	        @RequestParam String mail,
	        @RequestParam String adresse,
	        @RequestParam(required = false) String cgu,
	        @RequestParam(required = false) String perso,
	        Model model
	) {
	    // Renvoi des champs pré-remplis
	    model.addAttribute("login", login);
	    model.addAttribute("mail", mail);
	    model.addAttribute("adresse", adresse);
	    model.addAttribute("cgu", cgu);
	    model.addAttribute("perso", perso);

	    // Vérification CGU et données personnelles
	    if (cgu == null) {
	        model.addAttribute("erreur", "Vous devez accepter les Conditions Générales d’Utilisation.");
	        return "creer-utilisateur";
	    }
	    if (perso == null) {
	        model.addAttribute("erreur", "Vous devez accepter la conservation de vos données personnelles.");
	        return "creer-utilisateur";
	    }

	    // Vérification mot de passe : longueur + complexité
	    if (password.length() < 12) {
	        model.addAttribute("erreur", "Le mot de passe doit contenir au moins 12 caractères.");
	        return "creer-utilisateur";
	    }

	    int categories = 0;
	    if (password.matches(".*[A-Z].*")) categories++; // majuscule
	    if (password.matches(".*[a-z].*")) categories++; // minuscule
	    if (password.matches(".*\\d.*")) categories++;   // chiffre
	    if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) categories++; // spécial

	    if (categories < 3) {
	        model.addAttribute("erreur", "Le mot de passe doit contenir au moins 3 des 4 types suivants : majuscule, minuscule, chiffre, caractère spécial.");
	        return "creer-utilisateur";
	    }

	    // OK => créer le compte
	    String hashPassword = passwordEncoder.encode(password);
	    Utilisateur utilisateur = new Utilisateur(login, hashPassword, mail, "utilisateur", adresse);
	    utilisateurService.creerUtilisateur(utilisateur);

	    return "login";
	}

	@RequestMapping("/login")
	public  String  login(Model  model)  {
		return  "login";
	}
	@RequestMapping("/login-validation")
	public String login(String login, String password, Model model, HttpServletRequest request) {
	    System.out.println("==== login-validation ====");
	    System.out.println(login + " / " + password);

	    String hashPassword = null;
	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParLogin(login);

	    if (utilisateur == null || !passwordEncoder.matches(password, utilisateur.getPasswdHash())) {
	        model.addAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrect.");
	        return "login";
	    }

	    /* Vérifie si l'utilisateur existe et que le mot de passe correspond
	    if (utilisateur == null || !utilisateur.getPasswdHash().equals(hashPassword)) {
	        model.addAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrect.");
	        return "login";
	    } */
	    // Vérifie si le compte est bien activé
	    if (!utilisateur.isVerified()) {
	        model.addAttribute("erreur", "Votre compte n'est pas encore vérifié. Veuillez consulter votre boîte mail.");
	        return "login";
	    }
	    // Connexion réussie
	    request.getSession().setAttribute("id", utilisateur.getId());
	    request.getSession().setAttribute("login", utilisateur.getLogin());
	    request.getSession().setAttribute("role", utilisateur.getRole());

	    return "redirect:/profil";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		System.out.println("====  /logout  ====");
		request.getSession().invalidate();
		return "accueil";
	 }
	
	
	@GetMapping("/profil")
	public String afficherProfil(HttpServletRequest request, Model model) {
	    Long idUtilisateur = (Long) request.getSession().getAttribute("id");
	    if (idUtilisateur == null) {
	        return "redirect:/login";
	    }

	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParId(idUtilisateur);
	    model.addAttribute("utilisateur", utilisateur);
	    return "profil"; 
	}
	@PostMapping("/profil")
	public String mettreAJourProfil(
	        HttpServletRequest request,
	        @RequestParam String login,
	        @RequestParam String email,
	        @RequestParam String adresse,
	        @RequestParam(required = false) String ancienMdp,
	        @RequestParam(required = false) String nouveauMdp,
	        @RequestParam(required = false) String confirmationMdp,
	        RedirectAttributes redirectAttributes
	) {
	    Long id = (Long) request.getSession().getAttribute("id");
	    if (id == null) return "redirect:/login";

	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParId(id);
	    utilisateur.setLogin(login);
	    utilisateur.setEmail(email);
	    utilisateur.setAdresse(adresse);

	    if (ancienMdp != null && !ancienMdp.isBlank()) {
	        if (!passwordEncoder.matches(ancienMdp, utilisateur.getPasswdHash())) {
	            redirectAttributes.addFlashAttribute("erreur", "Ancien mot de passe incorrect.");
	            return "redirect:/profil";
	        }

	        if (!nouveauMdp.equals(confirmationMdp)) {
	            redirectAttributes.addFlashAttribute("erreur", "Les mots de passe ne correspondent pas.");
	            return "redirect:/profil";
	        }

	        // Vérifications CNIL
	        if (nouveauMdp.length() < 12) {
	            redirectAttributes.addFlashAttribute("erreur", "Le mot de passe doit contenir au moins 12 caractères.");
	            return "redirect:/profil";
	        }

	        int categories = 0;
	        if (nouveauMdp.matches(".*[A-Z].*")) categories++;
	        if (nouveauMdp.matches(".*[a-z].*")) categories++;
	        if (nouveauMdp.matches(".*\\d.*")) categories++;
	        if (nouveauMdp.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) categories++;

	        if (categories < 3) {
	            redirectAttributes.addFlashAttribute("erreur", "Le mot de passe doit contenir au moins 3 des 4 types suivants : majuscule, minuscule, chiffre, caractère spécial.");
	            return "redirect:/profil";
	        }

	        utilisateur.setPasswdHash(passwordEncoder.encode(nouveauMdp));
	    }
	    utilisateurService.creerUtilisateur(utilisateur);
	    redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès.");
	    return "redirect:/profil";
	}

	@GetMapping("/mot-de-passe-oublie")
	public String afficherFormulaireMotDePasseOublie(Model model) {
	    return "mot-de-passe-oublie";
	}
	@PostMapping("/mot-de-passe-oublie")
	public String traiterDemandeMotDePasseOublie(@RequestParam("email") String email, Model model) {
	    Utilisateur utilisateur = utilisateurRepository.findByEmail(email);

	    if (utilisateur != null) {
	        utilisateurService.envoyerLienReinitialisation(utilisateur); // ✅ envoi d'email réel
	        model.addAttribute("success", "Un lien de réinitialisation a été envoyé à votre adresse.");
	    } else {
	        model.addAttribute("erreur", "Aucun compte ne correspond à cette adresse email.");
	    }

	    return "mot-de-passe-oublie";
	}

	@GetMapping("/reinitialiser-mot-de-passe")
	public String afficherFormulaireReset(@RequestParam("token") String token, Model model) {
	    var optionalToken = verificationtokenRepository.findByToken(token);

	    if (optionalToken.isEmpty() || optionalToken.get().isExpired() || !"RESET".equals(optionalToken.get().getType())) {
	        model.addAttribute("erreur", "Le lien est invalide ou expiré.");
	        return "mot-de-passe-oublie";
	    }

	    model.addAttribute("token", token);
	    return "reinitialiser-mot-de-passe";
	}
	@PostMapping("/reinitialiser-mot-de-passe")
	public String traiterReset(@RequestParam("token") String token,
	                           @RequestParam("password") String password,
	                           @RequestParam("confirmation") String confirmation,
	                           Model model) {

	    if (!password.equals(confirmation)) {
	        model.addAttribute("erreur", "Les mots de passe ne correspondent pas.");
	        model.addAttribute("token", token);
	        return "reinitialiser-mot-de-passe";
	    }

	    // Vérification CNIL : Longueur minimale
	    if (password.length() < 12) {
	        model.addAttribute("erreur", "Le mot de passe doit contenir au moins 12 caractères.");
	        model.addAttribute("token", token);
	        return "reinitialiser-mot-de-passe";
	    }

	    // Vérification CNIL : Complexité
	    int categories = 0;
	    if (password.matches(".*[A-Z].*")) categories++;   // Majuscule
	    if (password.matches(".*[a-z].*")) categories++;   // Minuscule
	    if (password.matches(".*\\d.*")) categories++;     // Chiffre
	    if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) categories++; // Spécial

	    if (categories < 3) {
	        model.addAttribute("erreur", "Le mot de passe doit contenir au moins 3 des 4 types suivants : majuscule, minuscule, chiffre, caractère spécial.");
	        model.addAttribute("token", token);
	        return "reinitialiser-mot-de-passe";
	    }

	    var optionalToken = verificationtokenRepository.findByToken(token);

	    if (optionalToken.isEmpty() || optionalToken.get().isExpired() || !"RESET".equals(optionalToken.get().getType())) {
	        model.addAttribute("erreur", "Le lien est invalide ou expiré.");
	        return "mot-de-passe-oublie";
	    }

	    Utilisateur utilisateur = optionalToken.get().getUtilisateur();
	    utilisateur.setPasswdHash(passwordEncoder.encode(password));
	    utilisateurRepository.save(utilisateur);

	    verificationtokenRepository.delete(optionalToken.get());

	    model.addAttribute("success", "Votre mot de passe a bien été réinitialisé !");
	    return "login";
	}

}
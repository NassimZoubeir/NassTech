package com.ensup.nasstech.controller;

import java.security.NoSuchAlgorithmException;

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
import com.ensup.nasstech.outil.Outil;
import com.ensup.nasstech.service.UtilisateurServiceItf;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public  class  UtilisateurController  {
	
	@Autowired
	private UtilisateurServiceItf utilisateurService;
	
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
	public  String  creerUtilisateurValidation(String  login,  String  password,  String  mail, String adresse, String cgu)  {
		System.out.println(login  +  ",  "  +  password  +  ",  "  +  mail + ", " + adresse + ", " + cgu);
		
		 if (cgu == null) {
		        return "redirect:/creer-compte"; 
		    }
		 
		String  hashPassword  =  null;
		hashPassword = passwordEncoder.encode(password);
		Utilisateur  utilisateur  =  new  Utilisateur(login,  hashPassword,  mail, "utilisateur", adresse);
		utilisateurService.creerUtilisateur(utilisateur);
		return  "login";
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
	    hashPassword = passwordEncoder.encode(password);
	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParLogin(login);

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
	        try {
	            String ancienHash = Outil.hashMdpSha256(ancienMdp);
	            if (!utilisateur.getPasswdHash().equals(ancienHash)) {
	                redirectAttributes.addFlashAttribute("erreur", "Ancien mot de passe incorrect.");
	                return "redirect:/profil";
	            }
	            if (!nouveauMdp.equals(confirmationMdp)) {
	                redirectAttributes.addFlashAttribute("erreur", "Les mots de passe ne correspondent pas.");
	                return "redirect:/profil";
	            }
	            String nouveauHash = Outil.hashMdpSha256(nouveauMdp);
	            utilisateur.setPasswdHash(nouveauHash);
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("erreur", "Erreur technique.");
	            return "redirect:/profil";
	        }
	    }

	    utilisateurService.creerUtilisateur(utilisateur);
	    redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès.");
	    return "redirect:/profil";
	}


}
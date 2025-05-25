package com.ensup.nasstech.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.entity.Utilisateur;
import com.ensup.nasstech.outil.Outil;
import com.ensup.nasstech.service.UtilisateurServiceItf;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public  class  UtilisateurController  {
	
	@Autowired
	private UtilisateurServiceItf utilisateurService;

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
	public  String  creerUtilisateurValidation(String  login,  String  password,  String  mail)  {
		System.out.println(login  +  ",  "  +  password  +  ",  "  +  mail);
		String  hashPassword  =  null;
		try  {
			hashPassword  =  Outil.hashMdpSha256(password);
		}  catch  (NoSuchAlgorithmException  e)  {
			System.out.println("ERREUR  -  fonction  hashMdpSha256");
		}
		
		Utilisateur  utilisateur  =  new  Utilisateur(login,  hashPassword,  mail,  "abonne");
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
		try {
			hashPassword = Outil.hashMdpSha256(password);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERREUR - fonction hashMdpSha256");
		}
    	System.out.println("hashPassword=" + hashPassword);
    	Utilisateur utilisateur = utilisateurService.lireUtilisateurParLogin(login);
    	System.out.println("utilisateur:" + utilisateur);
    	if(utilisateur.getPasswdHash().equals(hashPassword)) {
    		System.out.println("Vous êtes connecté");
    		request.getSession().setAttribute("id",  utilisateur.getId());
			request.getSession().setAttribute("login",  utilisateur.getLogin());
			request.getSession().setAttribute("role",  utilisateur.getRole());
			return "redirect:/profil";
    	}
    	else System.out.println("Vous n'êtes pas connecté");
    	 model.addAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrect.");
    	return "login";
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
	        return "redirect:/login";  // pas connecté => retour à la page login
	    }

	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParId(idUtilisateur);
	    model.addAttribute("utilisateur", utilisateur);
	    return "profil";  // nom de la page Thymeleaf profil.html
	}


	@PostMapping("/profil")
	public String mettreAJourProfil(
	        HttpServletRequest request,
	        @RequestParam String login,
	        @RequestParam String email,
	        @RequestParam(required = false) String ancienMdp,
	        @RequestParam(required = false) String nouveauMdp,
	        @RequestParam(required = false) String confirmationMdp,
	        Model model
	) {
	    Long id = (Long) request.getSession().getAttribute("id");
	    if (id == null) return "redirect:/login";

	    Utilisateur utilisateur = utilisateurService.lireUtilisateurParId(id);

	    utilisateur.setLogin(login);
	    utilisateur.setEmail(email);

	    if (ancienMdp != null && !ancienMdp.isBlank()) {
	        try {
	            String ancienHash = Outil.hashMdpSha256(ancienMdp);
	            if (!utilisateur.getPasswdHash().equals(ancienHash)) {
	                model.addAttribute("utilisateur", utilisateur);
	                model.addAttribute("erreur", "Ancien mot de passe incorrect.");
	                return "profil";
	            }
	            if (!nouveauMdp.equals(confirmationMdp)) {
	                model.addAttribute("utilisateur", utilisateur);
	                model.addAttribute("erreur", "Les mots de passe ne correspondent pas.");
	                return "profil";
	            }

	            String nouveauHash = Outil.hashMdpSha256(nouveauMdp);
	            utilisateur.setPasswdHash(nouveauHash);
	        } catch (Exception e) {
	            model.addAttribute("utilisateur", utilisateur);
	            model.addAttribute("erreur", "Erreur technique.");
	            return "profil";
	        }
	    }

	    utilisateurService.creerUtilisateur(utilisateur); // utilise le save existant
	    model.addAttribute("utilisateur", utilisateur);
	    model.addAttribute("success", "Profil mis à jour avec succès.");
	    return "profil";
	}

}

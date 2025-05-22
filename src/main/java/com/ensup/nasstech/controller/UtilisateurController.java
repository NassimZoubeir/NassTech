package com.ensup.nasstech.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    	}
    	else System.out.println("Vous n'êtes pas connecté");
    	return "accueil";
    }
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		System.out.println("====  /logout  ====");
		request.getSession().invalidate();
		return "accueil";
	 }
}

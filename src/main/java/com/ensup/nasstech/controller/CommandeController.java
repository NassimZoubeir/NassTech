package com.ensup.nasstech.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ensup.nasstech.entity.Commande;
import com.ensup.nasstech.service.OrdinateurServiceItf;
import com.ensup.nasstech.service.UtilisateurServiceItf;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CommandeController {

    @Autowired
    private UtilisateurServiceItf utilisateurService;

    @Autowired
    private OrdinateurServiceItf ordinateurService;

    @RequestMapping("/valider-panier")
	public  String  validerPanier(Model  model,  HttpServletRequest  request)  {
		System.out.println("====  /valider-panier  ====");
		List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");
		System.out.println("ordinateurCommanderListId="  +  ordinateurCommanderListId);
		if(ordinateurCommanderListId  !=  null)  {
			Long  idUtilisateur  =  (Long)  request.getSession().getAttribute("id");
			utilisateurService.passerCommandeOrdinateurs(ordinateurCommanderListId,  idUtilisateur);
			request.getSession().removeAttribute("ordinateurAcheterListId");
		}
		else  System.out.println("Pas d'ordinateur acheté");
		return  "redirect:/afficher-commande";
	}
    @RequestMapping("/afficher-commande")
    public String afficherCommande(Model model, HttpServletRequest request) {
        System.out.println("==== /afficher-commande ====");
        Long idUtilisateur = (Long) request.getSession().getAttribute("id");
        List<Commande> commandeList = utilisateurService.getCommandeOrdinateurList(idUtilisateur);
        System.out.println("commandeList=" + commandeList);
        model.addAttribute("commandeList", commandeList);
        return "commande";
    }
}

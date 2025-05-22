package com.ensup.nasstech.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ensup.nasstech.entity.Achat;
import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.service.OrdinateurServiceItf;
import com.ensup.nasstech.service.UtilisateurServiceItf;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AchatController {

    @Autowired
    private UtilisateurServiceItf utilisateurService;

    @Autowired
    private OrdinateurServiceItf ordinateurService;

    @RequestMapping("/valider-panier")
	public  String  validerPanier(Model  model,  HttpServletRequest  request)  {
		System.out.println("====  /valider-panier  ====");
		List<Long>  ordinateurAcheterListId  =  (List<Long>)  request.getSession().getAttribute("ordinateurAcheterListId");
		System.out.println("ordinateurAcheterListId="  +  ordinateurAcheterListId);
		if(ordinateurAcheterListId  !=  null)  {
			Long  idUtilisateur  =  (Long)  request.getSession().getAttribute("id");
			utilisateurService.acheterListOrdinateurUtilisateur(ordinateurAcheterListId,  idUtilisateur);
			request.getSession().removeAttribute("ordinateurAcheterListId");
		}
		else  System.out.println("Pas d'ordinateur acheté");
		return  "redirect:/afficher-achat";
	}
    @RequestMapping("/afficher-achat")
    public String afficherAchat(Model model, HttpServletRequest request) {
        System.out.println("==== /afficher-achat ====");
        Long idUtilisateur = (Long) request.getSession().getAttribute("id");
        List<Achat> achatList = utilisateurService.getAchatOrdinateurList(idUtilisateur);
        System.out.println("achatList=" + achatList);
        model.addAttribute("titre", "Achat");
        model.addAttribute("achatList", achatList);
        return "achat";
    }


}

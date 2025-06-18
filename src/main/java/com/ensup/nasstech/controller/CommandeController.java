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
    public String validerPanier(Model model, HttpServletRequest request) {
        System.out.println("==== /valider-panier ====");

        List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");
        System.out.println("ordinateurCommanderListId = " + ordinateurCommanderListId);

        if (ordinateurCommanderListId != null && !ordinateurCommanderListId.isEmpty()) {
            Long idUtilisateur = (Long) request.getSession().getAttribute("id");

            if (idUtilisateur != null) {
                utilisateurService.passerCommandeOrdinateurs(ordinateurCommanderListId, idUtilisateur);

                for (Long id : ordinateurCommanderListId) {
                    ordinateurService.decrementernombreOrdinateur(id);
                }

                request.getSession().removeAttribute("ordinateurCommanderListId");
            } else {
                System.out.println("❌ Aucun utilisateur connecté !");
            }
        } else {
            System.out.println("❌ Aucun ordinateur dans le panier !");
        }

        return "redirect:/afficher-commande";
    }

    @RequestMapping("/afficher-commande")
    public String afficherCommande(Model model, HttpServletRequest request) {
        System.out.println("==== /afficher-commande ====");

        Long idUtilisateur = (Long) request.getSession().getAttribute("id");

        if (idUtilisateur != null) {
            List<Commande> commandeList = utilisateurService.getCommandeOrdinateurList(idUtilisateur);
            System.out.println("commandeList = " + commandeList);
            model.addAttribute("commandeList", commandeList);
        } else {
            System.out.println("❌ Aucun utilisateur connecté !");
            model.addAttribute("commandeList", List.of());
        }

        return "commande";
    }

    @RequestMapping("/success")
    public String paiementSuccess(HttpServletRequest request) {
        System.out.println("==== /success ====");

        List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");

        if (ordinateurCommanderListId != null && !ordinateurCommanderListId.isEmpty()) {
            Long idUtilisateur = (Long) request.getSession().getAttribute("id");

            if (idUtilisateur != null) {
                utilisateurService.passerCommandeOrdinateurs(ordinateurCommanderListId, idUtilisateur);

                for (Long id : ordinateurCommanderListId) {
                    ordinateurService.decrementernombreOrdinateur(id);
                }

                request.getSession().removeAttribute("ordinateurCommanderListId");
            } else {
                System.out.println("❌ Utilisateur non connecté dans /success !");
            }
        } else {
            System.out.println("❌ Aucun ordinateur à valider dans /success !");
        }

        return "redirect:/afficher-commande";
    }

    @RequestMapping("/cancel")
    public String paiementAnnule() {
        System.out.println("==== /cancel ====");
        return "redirect:/panier";
    }
}

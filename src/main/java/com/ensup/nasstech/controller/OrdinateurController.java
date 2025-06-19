package com.ensup.nasstech.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ensup.nasstech.entity.Marque;
import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.repository.MarqueRepository;
import com.ensup.nasstech.service.OrdinateurServiceItf;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;





@Controller
public class OrdinateurController {
	@Autowired
	private  OrdinateurServiceItf  ordinateurService;
	 
	@Autowired
    private MarqueRepository marqueRepository;
	
	@RequestMapping("/accueil")
	public  String  accueil()  {
		System.out.println("==== /accueils ====");
		return  "accueil";
	}
	@RequestMapping("/afficher-ordinateurs")
    public String administrer(Model model) {
		System.out.println("==== /afficher-ordinateurs ====");
		List<Ordinateur> ordinateurList = ordinateurService.getAllOrdinateur();
    	System.out.println("OrdinateurList: " + ordinateurList);
    	model.addAttribute("ordinateurList", ordinateurList);
        return "catalogue";
    }
	 @RequestMapping("/creer-ordinateur")
	 public String creerOrdinateur(Model model) {
	 model.addAttribute("titre", "Créer un ordinateur");
	 model.addAttribute("marques", marqueRepository.findAll());
	 return "creer-ordinateur";
	 }
	 @Autowired
	    private ServletContext context;

	    @Value("${dir.images}")
	    private String imageDir;
	    
	    @RequestMapping("/creer-ordinateur-validation")
	    public String creerValidationOrdinateur(
	            String denomination,
	            double prix,
	            double ecran,
	            String processeur,
	            int vive,
	            int nombreOrdinateur,
	            MultipartFile image,
	            Long marqueId) {

	    	String imageName = null;

	    	if (image != null && !image.isEmpty()) {
	    	    try {
	    	        imageName = image.getOriginalFilename();
	    	        System.out.println("imageDir = [" + imageDir + "]");
	    	        Path pathFile = Paths.get(imageDir, imageName);

	    	        System.out.println("Chemin de l'image : " + pathFile.toString());

	    	        File directory = new File(imageDir);
	    	        if (!directory.exists()) {
	    	            directory.mkdirs();
	    	        }
	    	        image.transferTo(pathFile.toFile());
	    	    } catch (IllegalStateException | IOException e) {
	    	        e.printStackTrace();
	    	        System.err.println("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
	    	        return "error"; 
	    	    }
	    	}
	    	
	    	 Marque marque = marqueRepository.findById(marqueId).orElse(null);
	         if (marque == null) {
	             System.err.println("Erreur: Marque non trouvée pour id=" + marqueId);
	             return "error";
	         }
	         
	    	Ordinateur ordinateur = new Ordinateur(denomination, prix, processeur, ecran, vive, imageName, nombreOrdinateur, marque);
	    	ordinateurService.creerOrdinateur(ordinateur);

	    	return "redirect:/afficher-ordinateurs";
	    }
	    @RequestMapping("/afficher-ordinateur/{id}")
		 public String afficherOrdinateur(@PathVariable Long id, Model model) {
			 System.out.println("==== /afficher-ordinateur ====");
			 System.out.println("id=" + id);
			 Ordinateur ordinateur = ordinateurService.getOrdinateurById(id);
			 System.out.println("Ordinateur=" + ordinateur);
			 model.addAttribute("ordinateur", ordinateur);
			 model.addAttribute("titre", ordinateur.getDenomination());
			 return "detail";
		 }
		 @RequestMapping("/acheter/{id}")
		    public String acheterOrdinateur(@PathVariable Long id, Model model, HttpServletRequest request) {
		    	System.out.println("==== /acheter/" + id + " ====");
		    	List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");
		    	if(ordinateurCommanderListId == null) {
		    		ordinateurCommanderListId = new ArrayList<>();
		    	}
		    	
		    	if(!ordinateurCommanderListId.contains(id)) {
		    		ordinateurCommanderListId.add(id);
		    	}
		    	request.getSession().setAttribute("ordinateurCommanderListId", ordinateurCommanderListId);
		    	System.out.println("ordinateurCommanderListId=" + ordinateurCommanderListId);
		    	return "redirect:/afficher-panier";
		    }
		 @RequestMapping("/afficher-panier")
			public String afficherPanier(Model model, HttpServletRequest request) {
				System.out.println("==== /afficher-panier ====");
				List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");
				System.out.println("ordinateurCommanderListId=" + ordinateurCommanderListId);
				if(ordinateurCommanderListId != null) {
					List<Ordinateur> ordinateurCommanderList = ordinateurService.getOrdinateurCommanderListParOrdinateurIdList(ordinateurCommanderListId);
					model.addAttribute("ordinateurCommanderList", ordinateurCommanderList);
				}
				else System.out.println("Pas d'ordinateur acheté");
				model.addAttribute("dénomination", "Achat d'ordinateur");
				return "panier";
			}
		 @RequestMapping("/supprimer-panier/{id}")
		 public String supprimerOrdinateurPanier(@PathVariable Long id, Model model, HttpServletRequest request) {
		     System.out.println("==== /supprimer-panier ====");
		     List<Long> ordinateurCommanderListId = (List<Long>) request.getSession().getAttribute("ordinateurCommanderListId");
		     System.out.println("ordinateurCommanderListId=" + ordinateurCommanderListId);
		     if (ordinateurCommanderListId != null) {
		         ordinateurCommanderListId.remove(id);
		         request.getSession().setAttribute("ordinateurCommanderListId", ordinateurCommanderListId);
		         System.out.println("ordinateurCommanderListId=" + ordinateurCommanderListId);
		     }
		     return "redirect:/afficher-panier";
		 }

		  @GetMapping("/recherche")
		    public String rechercherOrdinateurs(@RequestParam("query") String query, Model model) {
		        List<Ordinateur> resultats = ordinateurService.rechercherOrdinateurs(query);
		        model.addAttribute("ordinateurList", resultats);
		        model.addAttribute("query", query);
		        return "catalogue";
		    }
}